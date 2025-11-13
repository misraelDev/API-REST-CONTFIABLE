package com.contfiable.service.invoice;

import com.contfiable.dto.article.ArticleResponse;
import com.contfiable.dto.invoice.InvoiceCreateRequest;
import com.contfiable.dto.invoice.InvoiceResponse;
import com.contfiable.dto.invoice.InvoiceUpdateRequest;
import com.contfiable.exception.ResourceNotFoundException;
import com.contfiable.model.Invoice;
import com.contfiable.model.User;
import com.contfiable.repository.ArticleRepository;
import com.contfiable.repository.InvoiceRepository;
import com.contfiable.security.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ArticleRepository articleRepository;
    private final SecurityService securityService;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            ArticleRepository articleRepository,
            SecurityService securityService
    ) {
        this.invoiceRepository = invoiceRepository;
        this.articleRepository = articleRepository;
        this.securityService = securityService;
    }

    @Transactional
    public InvoiceResponse createInvoice(InvoiceCreateRequest request) {
        User currentUser = securityService.getCurrentUser();

        Invoice invoice = new Invoice();
        invoice.setUser(currentUser);
        invoice.setCustomerName(request.getCustomerName());
        invoice.setType(Optional.ofNullable(request.getType()).orElse(Invoice.Type.income));
        invoice.setStatus(Optional.ofNullable(request.getStatus()).orElse(Invoice.Status.draft));
        invoice.setCurrency(request.getCurrency());
        invoice.setPaymentMethod(request.getPaymentMethod());
        invoice.setUuidSat(request.getUuidSat());
        invoice.setDueDate(request.getDueDate());
        invoice.setPaidAt(request.getPaidAt());
        invoice.setCancelReason(request.getCancelReason());
        invoice.setNotes(request.getNotes());
        invoice.setPdfUrl(request.getPdfUrl());
        invoice.setXmlUrl(request.getXmlUrl());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return mapToResponse(savedInvoice);
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoice(Long id) {
        Long userId = securityService.getCurrentUserId();
        Invoice invoice = invoiceRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la factura con id %d".formatted(id)));
        return mapToResponse(invoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoices() {
        Long userId = securityService.getCurrentUserId();
        List<Invoice> invoices = invoiceRepository.findByUserId(userId);
        return invoices.stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public InvoiceResponse updateInvoice(Long id, InvoiceUpdateRequest request) {
        Long userId = securityService.getCurrentUserId();
        Invoice invoice = invoiceRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la factura con id %d".formatted(id)));

        if (StringUtils.hasText(request.getCustomerName())) {
            invoice.setCustomerName(request.getCustomerName());
        }
        if (request.getType() != null) {
            invoice.setType(request.getType());
        }
        if (request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }
        if (StringUtils.hasText(request.getCurrency())) {
            invoice.setCurrency(request.getCurrency());
        }
        if (request.getPaymentMethod() != null) {
            invoice.setPaymentMethod(request.getPaymentMethod());
        }
        if (request.getUuidSat() != null) {
            invoice.setUuidSat(request.getUuidSat());
        }
        if (request.getDueDate() != null) {
            invoice.setDueDate(request.getDueDate());
        }
        if (request.getPaidAt() != null) {
            invoice.setPaidAt(request.getPaidAt());
        }
        if (request.getCancelReason() != null) {
            invoice.setCancelReason(request.getCancelReason());
        }
        if (request.getNotes() != null) {
            invoice.setNotes(request.getNotes());
        }
        if (request.getPdfUrl() != null) {
            invoice.setPdfUrl(request.getPdfUrl());
        }
        if (request.getXmlUrl() != null) {
            invoice.setXmlUrl(request.getXmlUrl());
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return mapToResponse(savedInvoice);
    }

    @Transactional
    public void deleteInvoice(Long id) {
        Long userId = securityService.getCurrentUserId();
        Invoice invoice = invoiceRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la factura con id %d".formatted(id)));
        invoiceRepository.delete(invoice);
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        if (invoice.getId() == null) {
            return InvoiceResponse.fromEntity(invoice, List.of());
        }
        List<ArticleResponse> articles = articleRepository.findByInvoiceId(invoice.getId())
                .stream()
                .map(ArticleResponse::fromEntity)
                .toList();
        return InvoiceResponse.fromEntity(invoice, articles);
    }
}

