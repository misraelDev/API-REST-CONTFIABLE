package com.contfiable.dto.invoice;

import com.contfiable.model.Invoice;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceCreateRequest {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 200, message = "El nombre del cliente no puede exceder 200 caracteres")
    private String customerName;

    private Invoice.Type type;

    private Invoice.Status status;

    @NotBlank(message = "La moneda es obligatoria")
    @Size(min = 3, max = 3, message = "La moneda debe cumplir con el formato ISO 4217")
    private String currency;

    private Invoice.PaymentMethod paymentMethod;

    @Size(max = 36, message = "El UUID SAT no puede exceder 36 caracteres")
    private String uuidSat;

    private OffsetDateTime dueDate;

    private OffsetDateTime paidAt;

    @Size(max = 500, message = "El motivo de cancelación no puede exceder 500 caracteres")
    private String cancelReason;

    @Size(max = 2000, message = "Las notas no pueden exceder 2000 caracteres")
    private String notes;

    @Size(max = 500, message = "La URL del PDF no puede exceder 500 caracteres")
    private String pdfUrl;

    @Size(max = 500, message = "La URL del XML no puede exceder 500 caracteres")
    private String xmlUrl;

    @Valid
    private List<ArticleItem> articles = new ArrayList<>();

    // Clase interna para los artículos
    public static class ArticleItem {
        @NotBlank(message = "El nombre del artículo es obligatorio")
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
        private String name;

        @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
        private String description;

        @jakarta.validation.constraints.NotNull(message = "La cantidad es obligatoria")
        @jakarta.validation.constraints.DecimalMin(value = "0.01", message = "La cantidad debe ser mayor que 0")
        private BigDecimal quantity;

        @jakarta.validation.constraints.NotNull(message = "El precio es obligatorio")
        @jakarta.validation.constraints.DecimalMin(value = "0.00", message = "El precio debe ser mayor o igual que 0")
        private BigDecimal price;

        @jakarta.validation.constraints.DecimalMin(value = "0.00", message = "El impuesto debe ser mayor o igual que 0")
        private BigDecimal tax;

        @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
        private String imageUrl;

        // Getters y Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getQuantity() { return quantity; }
        public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public BigDecimal getTax() { return tax; }
        public void setTax(BigDecimal tax) { this.tax = tax; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Invoice.Type getType() {
        return type;
    }

    public void setType(Invoice.Type type) {
        this.type = type;
    }

    public Invoice.Status getStatus() {
        return status;
    }

    public void setStatus(Invoice.Status status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Invoice.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Invoice.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUuidSat() {
        return uuidSat;
    }

    public void setUuidSat(String uuidSat) {
        this.uuidSat = uuidSat;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public List<ArticleItem> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleItem> articles) {
        this.articles = articles != null ? articles : new ArrayList<>();
    }
}

