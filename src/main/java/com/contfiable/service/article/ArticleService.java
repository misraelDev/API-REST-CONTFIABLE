package com.contfiable.service.article;

import com.contfiable.dto.article.ArticleCreateRequest;
import com.contfiable.dto.article.ArticleResponse;
import com.contfiable.dto.article.ArticleUpdateRequest;
import com.contfiable.exception.ResourceNotFoundException;
import com.contfiable.model.Article;
import com.contfiable.model.Invoice;
import com.contfiable.repository.ArticleRepository;
import com.contfiable.repository.InvoiceRepository;
import com.contfiable.security.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final InvoiceRepository invoiceRepository;
    private final SecurityService securityService;

    public ArticleService(
            ArticleRepository articleRepository,
            InvoiceRepository invoiceRepository,
            SecurityService securityService
    ) {
        this.articleRepository = articleRepository;
        this.invoiceRepository = invoiceRepository;
        this.securityService = securityService;
    }

    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        Long userId = securityService.getCurrentUserId();
        Invoice invoice = invoiceRepository.findByIdAndUserId(request.getInvoiceId(), userId)
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
        Long userId = securityService.getCurrentUserId();
        Article article = articleRepository.findByIdAndInvoiceUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artículo con id %d".formatted(id)));
        return ArticleResponse.fromEntity(article);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticles(Long invoiceId) {
        Long userId = securityService.getCurrentUserId();
        List<Article> articles;
        if (invoiceId != null) {
            // Validar que la factura pertenezca al usuario
            invoiceRepository.findByIdAndUserId(invoiceId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró la factura con id %d".formatted(invoiceId)
                    ));
            articles = articleRepository.findByInvoiceId(invoiceId);
        } else {
            articles = articleRepository.findByInvoiceUserId(userId);
        }
        return articles.stream().map(ArticleResponse::fromEntity).toList();
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleUpdateRequest request) {
        Long userId = securityService.getCurrentUserId();
        Article article = articleRepository.findByIdAndInvoiceUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artículo con id %d".formatted(id)));

        updateEntityFromRequest(article, request.getName(), request.getDescription(),
                request.getQuantity(), request.getPrice(), request.getTax(), request.getImageUrl());

        Article saved = articleRepository.save(article);
        return ArticleResponse.fromEntity(saved);
    }

    @Transactional
    public void deleteArticle(Long id) {
        Long userId = securityService.getCurrentUserId();
        Article article = articleRepository.findByIdAndInvoiceUserId(id, userId)
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

