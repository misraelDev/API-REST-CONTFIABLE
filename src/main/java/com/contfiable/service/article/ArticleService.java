package com.contfiable.service.article;

import com.contfiable.dto.article.ArticleCreateRequest;
import com.contfiable.dto.article.ArticleResponse;
import com.contfiable.dto.article.ArticleUpdateRequest;
import com.contfiable.exception.ResourceNotFoundException;
import com.contfiable.model.Article;
import com.contfiable.model.Invoice;
import com.contfiable.repository.ArticleRepository;
import com.contfiable.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final InvoiceRepository invoiceRepository;

    public ArticleService(ArticleRepository articleRepository, InvoiceRepository invoiceRepository) {
        this.articleRepository = articleRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la factura con id %d".formatted(request.getInvoiceId())
                ));

        Article article = new Article();
        article.setInvoice(invoice);
        updateEntityFromRequest(article, request.getName(), request.getDescription(),
                request.getQuantity(), request.getPrice(), request.getTax(), request.getImageUrl());

        Article saved = articleRepository.save(article);
        return ArticleResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artículo con id %d".formatted(id)));
        return ArticleResponse.fromEntity(article);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticles(Long invoiceId) {
        List<Article> articles = invoiceId != null
                ? articleRepository.findByInvoiceId(invoiceId)
                : articleRepository.findAll();
        return articles.stream().map(ArticleResponse::fromEntity).toList();
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artículo con id %d".formatted(id)));

        updateEntityFromRequest(article, request.getName(), request.getDescription(),
                request.getQuantity(), request.getPrice(), request.getTax(), request.getImageUrl());

        Article saved = articleRepository.save(article);
        return ArticleResponse.fromEntity(saved);
    }

    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artículo con id %d".formatted(id)));
        articleRepository.delete(article);
    }

    private void updateEntityFromRequest(
            Article article,
            String name,
            String description,
            BigDecimal quantity,
            BigDecimal price,
            BigDecimal tax,
            String imageUrl
    ) {
        article.setName(name);
        article.setDescription(description);
        article.setQuantity(quantity);
        article.setPrice(price);
        article.setTax(tax);
        article.setImageUrl(imageUrl);
    }
}

