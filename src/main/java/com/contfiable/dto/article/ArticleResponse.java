package com.contfiable.dto.article;

import com.contfiable.model.Article;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ArticleResponse {

    private Long id;
    private Long invoiceId;
    private String name;
    private String description;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private String imageUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static ArticleResponse fromEntity(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.id = article.getId();
        response.invoiceId = article.getInvoice() != null ? article.getInvoice().getId() : null;
        response.name = article.getName();
        response.description = article.getDescription();
        response.quantity = article.getQuantity();
        response.price = article.getPrice();
        response.subtotal = article.getSubtotal();
        response.tax = article.getTax();
        response.total = article.getTotal();
        response.imageUrl = article.getImageUrl();
        response.createdAt = article.getCreatedAt();
        response.updatedAt = article.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}

