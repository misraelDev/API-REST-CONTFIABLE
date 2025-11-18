package com.contfiable.dto.invoice;

import com.contfiable.dto.article.ArticleResponse;
import com.contfiable.model.Invoice;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class InvoiceResponse {

    private Long id;
    private Long invoiceNumber;
    private String customerName;
    private Invoice.Type type;
    private Invoice.Status status;
    private Invoice.PaymentMethod paymentMethod;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private String currency;
    private String uuidSat;
    private OffsetDateTime dueDate;
    private OffsetDateTime paidAt;
    private String cancelReason;
    private String notes;
    private String pdfUrl;
    private String xmlUrl;
    private String imageUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<ArticleResponse> articles;

    public static InvoiceResponse fromEntity(Invoice invoice, List<ArticleResponse> articles) {
        InvoiceResponse response = new InvoiceResponse();
        response.id = invoice.getId();
        response.invoiceNumber = invoice.getInvoiceNumber();
        response.customerName = invoice.getCustomerName();
        response.type = invoice.getType();
        response.status = invoice.getStatus();
        response.paymentMethod = invoice.getPaymentMethod();
        response.subtotal = invoice.getSubtotal();
        response.tax = invoice.getTax();
        response.total = invoice.getTotal();
        response.currency = invoice.getCurrency();
        response.uuidSat = invoice.getUuidSat();
        response.dueDate = invoice.getDueDate();
        response.paidAt = invoice.getPaidAt();
        response.cancelReason = invoice.getCancelReason();
        response.notes = invoice.getNotes();
        response.pdfUrl = invoice.getPdfUrl();
        response.xmlUrl = invoice.getXmlUrl();
        response.imageUrl = invoice.getImageUrl();
        response.createdAt = invoice.getCreatedAt();
        response.updatedAt = invoice.getUpdatedAt();
        response.articles = articles != null ? List.copyOf(articles) : List.of();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Invoice.Type getType() {
        return type;
    }

    public Invoice.Status getStatus() {
        return status;
    }

    public Invoice.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUuidSat() {
        return uuidSat;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public String getNotes() {
        return notes;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<ArticleResponse> getArticles() {
        return articles;
    }
}

