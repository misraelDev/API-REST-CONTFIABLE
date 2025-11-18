package com.contfiable.service.invoice;

import com.contfiable.dto.article.ArticleResponse;
import com.contfiable.dto.invoice.InvoiceCreateRequest;
import com.contfiable.dto.invoice.InvoiceResponse;
import com.contfiable.dto.invoice.InvoiceSummaryResponse;
import com.contfiable.dto.invoice.InvoiceUpdateRequest;
import com.contfiable.exception.ResourceNotFoundException;
import com.contfiable.model.Article;
import com.contfiable.model.Invoice;
import com.contfiable.model.User;
import com.contfiable.repository.ArticleRepository;
import com.contfiable.repository.InvoiceRepository;
import com.contfiable.security.SecurityService;
import com.contfiable.service.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ArticleRepository articleRepository;
    private final SecurityService securityService;
    private final FileStorageService fileStorageService;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            ArticleRepository articleRepository,
            SecurityService securityService,
            FileStorageService fileStorageService
    ) {
        this.invoiceRepository = invoiceRepository;
        this.articleRepository = articleRepository;
        this.securityService = securityService;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public InvoiceResponse createInvoice(InvoiceCreateRequest request, MultipartFile pdfFile, MultipartFile xmlFile, List<MultipartFile> articleImages) {
        User currentUser = securityService.getCurrentUser();

        // Generar número de factura automáticamente
        Long nextInvoiceNumber = invoiceRepository.findMaxInvoiceNumber() + 1;

        Invoice invoice = new Invoice();
        invoice.setUser(currentUser);
        invoice.setInvoiceNumber(nextInvoiceNumber);
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

        // Guardar archivos PDF y XML
        if (pdfFile != null && !pdfFile.isEmpty()) {
            String pdfUrl = fileStorageService.storeFile(pdfFile, "invoices/pdf");
            invoice.setPdfUrl(pdfUrl);
        }
        if (xmlFile != null && !xmlFile.isEmpty()) {
            String xmlUrl = fileStorageService.storeFile(xmlFile, "invoices/xml");
            invoice.setXmlUrl(xmlUrl);
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        
        if (request.getArticles() != null && !request.getArticles().isEmpty()) {
            int imageIndex = 0;
            for (InvoiceCreateRequest.ArticleItem articleItem : request.getArticles()) {
                Article article = new Article();
                article.setInvoice(savedInvoice);
                article.setName(articleItem.getName());
                article.setDescription(articleItem.getDescription());
                article.setQuantity(articleItem.getQuantity());
                article.setPrice(articleItem.getPrice());
                article.setTax(articleItem.getTax() != null ? articleItem.getTax() : java.math.BigDecimal.ZERO);
                
                if (articleImages != null && imageIndex < articleImages.size()) {
                    MultipartFile imageFile = articleImages.get(imageIndex);
                    if (imageFile != null && !imageFile.isEmpty()) {
                        String imageUrl = fileStorageService.storeFile(imageFile, "articles/images");
                        article.setImageUrl(imageUrl);
                    }
                    imageIndex++;
                }
                
                savedInvoice.addArticle(article);
            }
            
            savedInvoice = invoiceRepository.saveAndFlush(savedInvoice);
        }

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

    @Transactional(readOnly = true)
    public List<InvoiceSummaryResponse> getInvoicesSummary() {
        Long userId = securityService.getCurrentUserId();
        List<Invoice> invoices = invoiceRepository.findByUserId(userId);
        
        final BigDecimal sumaTotalIncome = invoices.stream()
                .filter(invoice -> invoice.getType() == Invoice.Type.income && invoice.getTotal() != null)
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        final BigDecimal sumaTotalExpense = invoices.stream()
                .filter(invoice -> invoice.getType() == Invoice.Type.expense && invoice.getTotal() != null)
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        final BigDecimal balance = sumaTotalIncome.subtract(sumaTotalExpense);
        
        return invoices.stream().map(invoice -> {
            InvoiceSummaryResponse summary = new InvoiceSummaryResponse(
                invoice.getId(),
                invoice.getCustomerName(),
                invoice.getType().name(),
                invoice.getTotal(),
                invoice.getCreatedAt()
            );
            summary.setSumaTotalIncome(sumaTotalIncome);
            summary.setSumaTotalExpense(sumaTotalExpense);
            summary.setBalance(balance);
            return summary;
        }).toList();
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

