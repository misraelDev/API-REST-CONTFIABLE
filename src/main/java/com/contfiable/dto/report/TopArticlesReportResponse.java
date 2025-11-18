package com.contfiable.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TopArticlesReportResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ArticleItem> articles;

    public TopArticlesReportResponse() {}

    public TopArticlesReportResponse(LocalDate startDate, LocalDate endDate, List<ArticleItem> articles) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.articles = articles;
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

    public List<ArticleItem> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleItem> articles) {
        this.articles = articles;
    }

    public static class ArticleItem {
        private String articleName;
        private Integer quantitySold;
        private BigDecimal totalRevenue;
        private Integer invoiceCount;

        public ArticleItem() {}

        public ArticleItem(String articleName, Integer quantitySold, BigDecimal totalRevenue, Integer invoiceCount) {
            this.articleName = articleName;
            this.quantitySold = quantitySold;
            this.totalRevenue = totalRevenue;
            this.invoiceCount = invoiceCount;
        }

        public String getArticleName() {
            return articleName;
        }

        public void setArticleName(String articleName) {
            this.articleName = articleName;
        }

        public Integer getQuantitySold() {
            return quantitySold;
        }

        public void setQuantitySold(Integer quantitySold) {
            this.quantitySold = quantitySold;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public Integer getInvoiceCount() {
            return invoiceCount;
        }

        public void setInvoiceCount(Integer invoiceCount) {
            this.invoiceCount = invoiceCount;
        }
    }
}
