package com.contfiable.repository;

import com.contfiable.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByUserId(Long userId);
    
    Optional<Invoice> findByIdAndUserId(Long id, Long userId);
    
    @Query("SELECT COALESCE(MAX(i.invoiceNumber), 0) FROM Invoice i")
    Long findMaxInvoiceNumber();
}

