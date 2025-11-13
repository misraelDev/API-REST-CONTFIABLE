package com.contfiable.repository;

import com.contfiable.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByInvoiceId(Long invoiceId);

    @Query("SELECT a FROM Article a WHERE a.id = :id AND a.invoice.user.id = :userId")
    Optional<Article> findByIdAndInvoiceUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT a FROM Article a WHERE a.invoice.user.id = :userId")
    List<Article> findByInvoiceUserId(@Param("userId") Long userId);
}

