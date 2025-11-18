package com.contfiable.service.report;

import com.contfiable.dto.report.CashFlowReportResponse;
import com.contfiable.dto.report.IncomeVsExpenseReportResponse;
import com.contfiable.dto.report.TopArticlesReportResponse;
import com.contfiable.dto.report.TopCustomersReportResponse;
import com.contfiable.model.Article;
import com.contfiable.model.Invoice;
import com.contfiable.repository.ArticleRepository;
import com.contfiable.repository.InvoiceRepository;
import com.contfiable.security.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final InvoiceRepository invoiceRepository;
    private final ArticleRepository articleRepository;
    private final SecurityService securityService;

    public ReportService(InvoiceRepository invoiceRepository, ArticleRepository articleRepository, SecurityService securityService) {
        this.invoiceRepository = invoiceRepository;
        this.articleRepository = articleRepository;
        this.securityService = securityService;
    }

    @Transactional(readOnly = true)
    public IncomeVsExpenseReportResponse getIncomeVsExpenseReport(LocalDate since, LocalDate until) {
        Long userId = securityService.getCurrentUserId();
        List<Invoice> invoices = getInvoicesByDateRange(userId, since, until);

        BigDecimal totalIncome = invoices.stream()
                .filter(inv -> inv.getType() == Invoice.Type.income && inv.getTotal() != null)
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = invoices.stream()
                .filter(inv -> inv.getType() == Invoice.Type.expense && inv.getTotal() != null)
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        int incomeCount = (int) invoices.stream().filter(inv -> inv.getType() == Invoice.Type.income).count();
        int expenseCount = (int) invoices.stream().filter(inv -> inv.getType() == Invoice.Type.expense).count();

        return new IncomeVsExpenseReportResponse(
                since, until, totalIncome, totalExpense, balance,
                invoices.size(), incomeCount, expenseCount
        );
    }

    @Transactional(readOnly = true)
    public CashFlowReportResponse getCashFlowReport(LocalDate since, LocalDate until) {
        Long userId = securityService.getCurrentUserId();
        List<Invoice> invoices = getInvoicesByDateRange(userId, since, until);

        List<Invoice> beforePeriod = invoiceRepository.findByUserId(userId).stream()
                .filter(inv -> inv.getCreatedAt().toLocalDate().isBefore(since))
                .toList();

        BigDecimal initialIncome = beforePeriod.stream()
                .filter(inv -> inv.getType() == Invoice.Type.income && inv.getTotal() != null)
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal initialExpense = beforePeriod.stream()
                .filter(inv -> inv.getType() == Invoice.Type.expense && inv.getTotal() != null)
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal initialBalance = initialIncome.subtract(initialExpense);

        List<Invoice> sortedInvoices = invoices.stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .toList();

        BigDecimal accumulated = initialBalance;
        List<CashFlowReportResponse.CashFlowItem> items = new ArrayList<>();

        for (Invoice invoice : sortedInvoices) {
            BigDecimal amount = invoice.getTotal() != null ? invoice.getTotal() : BigDecimal.ZERO;
            if (invoice.getType() == Invoice.Type.income) {
                accumulated = accumulated.add(amount);
            } else {
                accumulated = accumulated.subtract(amount);
            }

            items.add(new CashFlowReportResponse.CashFlowItem(
                    invoice.getCreatedAt().toLocalDate(),
                    invoice.getId(),
                    invoice.getCustomerName(),
                    invoice.getType().name(),
                    amount,
                    accumulated
            ));
        }

        return new CashFlowReportResponse(since, until, initialBalance, accumulated, items);
    }

    @Transactional(readOnly = true)
    public TopCustomersReportResponse getTopCustomersReport(LocalDate since, LocalDate until) {
        Long userId = securityService.getCurrentUserId();
        List<Invoice> invoices = getInvoicesByDateRange(userId, since, until).stream()
                .filter(inv -> inv.getType() == Invoice.Type.income)
                .toList();

        Map<String, List<Invoice>> byCustomer = invoices.stream()
                .collect(Collectors.groupingBy(Invoice::getCustomerName));

        List<TopCustomersReportResponse.CustomerItem> customers = byCustomer.entrySet().stream()
                .map(entry -> {
                    String customerName = entry.getKey();
                    List<Invoice> customerInvoices = entry.getValue();
                    BigDecimal total = customerInvoices.stream()
                            .map(inv -> inv.getTotal() != null ? inv.getTotal() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new TopCustomersReportResponse.CustomerItem(customerName, total, customerInvoices.size());
                })
                .sorted((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()))
                .toList();

        return new TopCustomersReportResponse(since, until, customers);
    }

    @Transactional(readOnly = true)
    public TopArticlesReportResponse getTopArticlesReport(LocalDate since, LocalDate until) {
        Long userId = securityService.getCurrentUserId();
        List<Invoice> invoices = getInvoicesByDateRange(userId, since, until).stream()
                .filter(inv -> inv.getType() == Invoice.Type.income)
                .toList();

        List<Long> invoiceIds = invoices.stream().map(Invoice::getId).toList();

        List<Article> articles = new ArrayList<>();
        for (Long invoiceId : invoiceIds) {
            articles.addAll(articleRepository.findByInvoiceId(invoiceId));
        }

        Map<String, List<Article>> byArticleName = articles.stream()
                .collect(Collectors.groupingBy(Article::getName));

        List<TopArticlesReportResponse.ArticleItem> articleItems = byArticleName.entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    List<Article> articleList = entry.getValue();
                    BigDecimal totalQty = articleList.stream()
                            .map(art -> art.getQuantity() != null ? art.getQuantity() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal totalRevenue = articleList.stream()
                            .map(art -> {
                                BigDecimal price = art.getPrice() != null ? art.getPrice() : BigDecimal.ZERO;
                                BigDecimal qty = art.getQuantity() != null ? art.getQuantity() : BigDecimal.ZERO;
                                return price.multiply(qty);
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new TopArticlesReportResponse.ArticleItem(name, totalQty.intValue(), totalRevenue, articleList.size());
                })
                .sorted((a, b) -> b.getTotalRevenue().compareTo(a.getTotalRevenue()))
                .toList();

        return new TopArticlesReportResponse(since, until, articleItems);
    }

    private List<Invoice> getInvoicesByDateRange(Long userId, LocalDate since, LocalDate until) {
        return invoiceRepository.findByUserId(userId).stream()
                .filter(inv -> {
                    LocalDate invoiceDate = inv.getCreatedAt().atZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
                    return !invoiceDate.isBefore(since) && !invoiceDate.isAfter(until);
                })
                .toList();
    }
}
