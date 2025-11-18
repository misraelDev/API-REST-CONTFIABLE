package com.contfiable.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TopCustomersReportResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CustomerItem> customers;

    public TopCustomersReportResponse() {}

    public TopCustomersReportResponse(LocalDate startDate, LocalDate endDate, List<CustomerItem> customers) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customers = customers;
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

    public List<CustomerItem> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerItem> customers) {
        this.customers = customers;
    }

    public static class CustomerItem {
        private String customerName;
        private BigDecimal totalAmount;
        private Integer invoiceCount;

        public CustomerItem() {}

        public CustomerItem(String customerName, BigDecimal totalAmount, Integer invoiceCount) {
            this.customerName = customerName;
            this.totalAmount = totalAmount;
            this.invoiceCount = invoiceCount;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Integer getInvoiceCount() {
            return invoiceCount;
        }

        public void setInvoiceCount(Integer invoiceCount) {
            this.invoiceCount = invoiceCount;
        }
    }
}
