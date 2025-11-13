package com.contfiable.repository;

import com.contfiable.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByInvoiceId(Long invoiceId);
}

