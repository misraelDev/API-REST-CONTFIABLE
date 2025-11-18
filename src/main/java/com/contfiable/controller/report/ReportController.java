package com.contfiable.controller.report;

import com.contfiable.dto.report.CashFlowReportResponse;
import com.contfiable.dto.report.IncomeVsExpenseReportResponse;
import com.contfiable.dto.report.TopArticlesReportResponse;
import com.contfiable.dto.report.TopCustomersReportResponse;
import com.contfiable.service.report.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/income-vs-expense")
    public ResponseEntity<IncomeVsExpenseReportResponse> getIncomeVsExpenseReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
    ) {
        IncomeVsExpenseReportResponse response = reportService.getIncomeVsExpenseReport(since, until);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cash-flow")
    public ResponseEntity<CashFlowReportResponse> getCashFlowReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
    ) {
        CashFlowReportResponse response = reportService.getCashFlowReport(since, until);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-customers")
    public ResponseEntity<TopCustomersReportResponse> getTopCustomersReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
    ) {
        TopCustomersReportResponse response = reportService.getTopCustomersReport(since, until);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-articles")
    public ResponseEntity<TopArticlesReportResponse> getTopArticlesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
    ) {
        TopArticlesReportResponse response = reportService.getTopArticlesReport(since, until);
        return ResponseEntity.ok(response);
    }
}
