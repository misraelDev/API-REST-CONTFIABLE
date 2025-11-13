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
import com.contfiable.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            UserRepository userRepository,
            ArticleRepository articleRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public InvoiceResponse createInvoice(InvoiceCreateRequest request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El cliente con id %d no existe".formatted(request.getCustomerId())
                ));

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
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
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la factura con id %d".formatted(id)));
        return mapToResponse(invoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoices(Long customerId) {
        List<Invoice> invoices = customerId != null
                ? invoiceRepository.findByCustomerId(customerId)
                : invoiceRepository.findAll();
        return invoices.stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public InvoiceResponse updateInvoice(Long id, InvoiceUpdateRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la factura con id %d".formatted(id)));

        if (request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }
        if (request.getCurrency() != null) {
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
        Invoice invoice = invoiceRepository.findById(id)
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

