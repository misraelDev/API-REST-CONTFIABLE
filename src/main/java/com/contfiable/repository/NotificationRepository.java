package com.contfiable.repository;

import com.contfiable.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Notification.Status status);
    
    Optional<Notification> findByIdAndUserId(Long id, Long userId);
    
    Long countByUserIdAndStatus(Long userId, Notification.Status status);
    
    void deleteByUserId(Long userId);
}
