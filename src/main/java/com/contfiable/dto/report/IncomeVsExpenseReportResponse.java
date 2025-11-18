package com.contfiable.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IncomeVsExpenseReportResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private Integer totalInvoices;
    private Integer incomeInvoices;
    private Integer expenseInvoices;

    public IncomeVsExpenseReportResponse() {}

    public IncomeVsExpenseReportResponse(LocalDate startDate, LocalDate endDate, BigDecimal totalIncome, 
                                         BigDecimal totalExpense, BigDecimal balance, Integer totalInvoices, 
                                         Integer incomeInvoices, Integer expenseInvoices) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
        this.totalInvoices = totalInvoices;
        this.incomeInvoices = incomeInvoices;
        this.expenseInvoices = expenseInvoices;
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

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getTotalInvoices() {
        return totalInvoices;
    }

    public void setTotalInvoices(Integer totalInvoices) {
        this.totalInvoices = totalInvoices;
    }

    public Integer getIncomeInvoices() {
        return incomeInvoices;
    }

    public void setIncomeInvoices(Integer incomeInvoices) {
        this.incomeInvoices = incomeInvoices;
    }

    public Integer getExpenseInvoices() {
        return expenseInvoices;
    }

    public void setExpenseInvoices(Integer expenseInvoices) {
        this.expenseInvoices = expenseInvoices;
    }
}
