package com.contfiable.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices", schema = "public")
public class Invoice {

    public enum Status {
        draft,
        issued,
        paid,
        cancelled,
        overdue
    }

    public enum PaymentMethod {
        transfer,
        card,
        cash,
        check,
        other
    }

    public enum Type {
        income,
        expense
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "El usuario es requerido")
    private User user;

    @Column(name = "customer_name", length = 200)
    @NotBlank(message = "El nombre del cliente es requerido")
    @Size(max = 200, message = "El nombre del cliente no puede exceder 200 caracteres")
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type = Type.income;

    @Column(name = "invoice_number", unique = true)
    private Long invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.draft;

    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax", precision = 15, scale = 2)
    private BigDecimal tax;

    @Column(name = "total", precision = 15, scale = 2)
    private BigDecimal total;

    @Column(name = "currency", length = 3)
    @NotBlank(message = "La moneda es requerida")
    @Size(min = 3, max = 3, message = "La moneda debe tener 3 caracteres (ISO 4217)")
    private String currency = "MXN";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "uuid_sat", length = 36)
    @Size(max = 36, message = "El UUID SAT no puede exceder 36 caracteres")
    private String uuidSat;

    @Column(name = "due_date")
    private OffsetDateTime dueDate;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    @Column(name = "cancel_reason", length = 500)
    @Size(max = 500, message = "El motivo de cancelación no puede exceder 500 caracteres")
    private String cancelReason;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "pdf_url", length = 500)
    @Size(max = 500, message = "La URL del PDF no puede exceder 500 caracteres")
    private String pdfUrl;

    @Column(name = "xml_url", length = 500)
    @Size(max = 500, message = "La URL del XML no puede exceder 500 caracteres")
    private String xmlUrl;

    @Column(name = "image_url", length = 500)
    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    private String imageUrl;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addArticle(Article article) {
        article.setInvoice(this);
        this.articles.add(article);
        calculateTotals();
    }

    public void removeArticle(Article article) {
        article.setInvoice(null);
        this.articles.remove(article);
    }

    // Lifecycle and totals
    @PrePersist
    private void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        calculateTotals();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
        calculateTotals();
    }

    public void calculateTotals() {
        java.math.BigDecimal sub = java.math.BigDecimal.ZERO;
        java.math.BigDecimal taxSum = java.math.BigDecimal.ZERO;
        if (this.articles != null) {
            for (Article a : this.articles) {
                if (a.getSubtotal() != null) sub = sub.add(a.getSubtotal());
                if (a.getTax() != null) taxSum = taxSum.add(a.getTax());
            }
        }
        this.subtotal = sub;
        this.tax = taxSum;
        this.total = (this.subtotal != null ? this.subtotal : java.math.BigDecimal.ZERO)
                .add(this.tax != null ? this.tax : java.math.BigDecimal.ZERO);
    }
}
