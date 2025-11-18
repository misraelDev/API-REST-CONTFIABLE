package com.contfiable.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CashFlowReportResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal initialBalance;
    private BigDecimal finalBalance;
    private List<CashFlowItem> items;

    public CashFlowReportResponse() {}

    public CashFlowReportResponse(LocalDate startDate, LocalDate endDate, BigDecimal initialBalance, 
                                  BigDecimal finalBalance, List<CashFlowItem> items) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
        this.items = items;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(BigDecimal finalBalance) {
        this.finalBalance = finalBalance;
    }

    public List<CashFlowItem> getItems() {
        return items;
    }

    public void setItems(List<CashFlowItem> items) {
        this.items = items;
    }

    public static class CashFlowItem {
        private LocalDate date;
        private Long invoiceId;
        private String customerName;
        private String type;
        private BigDecimal amount;
        private BigDecimal accumulatedBalance;

        public CashFlowItem() {}

        public CashFlowItem(LocalDate date, Long invoiceId, String customerName, String type, 
                           BigDecimal amount, BigDecimal accumulatedBalance) {
            this.date = date;
            this.invoiceId = invoiceId;
            this.customerName = customerName;
            this.type = type;
            this.amount = amount;
            this.accumulatedBalance = accumulatedBalance;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Long getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(Long invoiceId) {
            this.invoiceId = invoiceId;
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

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getAccumulatedBalance() {
            return accumulatedBalance;
        }

        public void setAccumulatedBalance(BigDecimal accumulatedBalance) {
            this.accumulatedBalance = accumulatedBalance;
        }
    }
}
