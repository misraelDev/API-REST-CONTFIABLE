package com.contfiable.dto.invoice;

import java.math.BigDecimal;
import java.util.List;

public class InvoicesSummaryWrapper {
    private GlobalSummary summary;
    private List<InvoiceSummaryItem> invoices;

    public InvoicesSummaryWrapper() {}

    public InvoicesSummaryWrapper(GlobalSummary summary, List<InvoiceSummaryItem> invoices) {
        this.summary = summary;
        this.invoices = invoices;
    }

    public GlobalSummary getSummary() {
        return summary;
    }

    public void setSummary(GlobalSummary summary) {
        this.summary = summary;
    }

    public List<InvoiceSummaryItem> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceSummaryItem> invoices) {
        this.invoices = invoices;
    }

    public static class GlobalSummary {
        private BigDecimal sumaTotalIncome;
        private BigDecimal sumaTotalExpense;
        private BigDecimal balance;

        public GlobalSummary() {}

        public GlobalSummary(BigDecimal sumaTotalIncome, BigDecimal sumaTotalExpense, BigDecimal balance) {
            this.sumaTotalIncome = sumaTotalIncome;
            this.sumaTotalExpense = sumaTotalExpense;
            this.balance = balance;
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
}
