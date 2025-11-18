package com.contfiable.dto.invoice;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class InvoiceSummaryResponse {
    private Long id;
    private String customerName;
    private String type;
    private BigDecimal total;
    private OffsetDateTime createdAt;
    private BigDecimal sumaTotalIncome;
    private BigDecimal sumaTotalExpense;
    private BigDecimal balance;

    public InvoiceSummaryResponse() {}

    public InvoiceSummaryResponse(Long id, String customerName, String type, BigDecimal total, OffsetDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.type = type;
        this.total = total;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getSumaTotalIncome() {
        return sumaTotalIncome;
    }

    public void setSumaTotalIncome(BigDecimal sumaTotalIncome) {
        this.sumaTotalIncome = sumaTotalIncome;
    }

    public BigDecimal getSumaTotalExpense() {
        return sumaTotalExpense;
    }

    public void setSumaTotalExpense(BigDecimal sumaTotalExpense) {
        this.sumaTotalExpense = sumaTotalExpense;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
