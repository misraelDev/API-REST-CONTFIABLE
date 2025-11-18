package com.contfiable.service.notification;

import com.contfiable.dto.notification.NotificationCreateRequest;
import com.contfiable.dto.notification.NotificationResponse;
import com.contfiable.exception.ResourceNotFoundException;
import com.contfiable.model.Invoice;
import com.contfiable.model.Notification;
import com.contfiable.model.User;
import com.contfiable.repository.InvoiceRepository;
import com.contfiable.repository.NotificationRepository;
import com.contfiable.security.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final InvoiceRepository invoiceRepository;
    private final SecurityService securityService;

    public NotificationService(NotificationRepository notificationRepository, 
                              InvoiceRepository invoiceRepository,
                              SecurityService securityService) {
        this.notificationRepository = notificationRepository;
        this.invoiceRepository = invoiceRepository;
        this.securityService = securityService;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications() {
        Long userId = securityService.getCurrentUserId();
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return notifications.stream().map(NotificationResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications() {
        Long userId = securityService.getCurrentUserId();
        List<Notification> notifications = notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(
            userId, Notification.Status.unread
        );
        return notifications.stream().map(NotificationResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount() {
        Long userId = securityService.getCurrentUserId();
        return notificationRepository.countByUserIdAndStatus(userId, Notification.Status.unread);
    }

    @Transactional
    public NotificationResponse markAsRead(Long notificationId) {
        Long userId = securityService.getCurrentUserId();
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));
        
        notification.markAsRead();
        notification = notificationRepository.save(notification);
        return NotificationResponse.fromEntity(notification);
    }

    @Transactional
    public void markAllAsRead() {
        Long userId = securityService.getCurrentUserId();
        List<Notification> notifications = notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(
            userId, Notification.Status.unread
        );
        notifications.forEach(Notification::markAsRead);
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        User currentUser = securityService.getCurrentUser();

        Notification notification = new Notification();
        notification.setUser(currentUser);
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());

        if (request.getInvoiceId() != null) {
            Invoice invoice = invoiceRepository.findByIdAndUserId(request.getInvoiceId(), currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada"));
            notification.setInvoice(invoice);
        }

        notification = notificationRepository.save(notification);
        return NotificationResponse.fromEntity(notification);
    }

    @Transactional
    public void createInvoiceNotification(Long invoiceId, Notification.Type type, String title, String message) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada"));

        Notification notification = new Notification();
        notification.setUser(invoice.getUser());
        notification.setInvoice(invoice);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);

        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        Long userId = securityService.getCurrentUserId();
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));
        notificationRepository.delete(notification);
    }
}
