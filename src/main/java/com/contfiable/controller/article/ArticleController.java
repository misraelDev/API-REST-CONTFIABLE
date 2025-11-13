package com.contfiable.controller.article;

import com.contfiable.dto.article.ArticleCreateRequest;
import com.contfiable.dto.article.ArticleResponse;
import com.contfiable.dto.article.ArticleUpdateRequest;
import com.contfiable.service.article.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@Valid @RequestBody ArticleCreateRequest request) {
        ArticleResponse response = articleService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long articleId) {
        ArticleResponse response = articleService.getArticle(articleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getArticles(
            @RequestParam(name = "invoiceId", required = false) Long invoiceId
    ) {
        List<ArticleResponse> responses = articleService.getArticles(invoiceId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long articleId,
            @Valid @RequestBody ArticleUpdateRequest request
    ) {
        ArticleResponse response = articleService.updateArticle(articleId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.noContent().build();
    }
}

