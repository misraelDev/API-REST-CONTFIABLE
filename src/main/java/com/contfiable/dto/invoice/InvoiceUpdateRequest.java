package com.contfiable.dto.invoice;

import com.contfiable.model.Invoice;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public class InvoiceUpdateRequest {

    private String customerName;

    private Invoice.Type type;

    private Invoice.Status status;

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
}

