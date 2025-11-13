package com.contfiable.repository;

import com.contfiable.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByUserId(Long userId);
    
    Optional<Invoice> findByIdAndUserId(Long id, Long userId);
}

