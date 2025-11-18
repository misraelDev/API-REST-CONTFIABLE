package com.contfiable.scheduler;

import com.contfiable.model.Invoice;
import com.contfiable.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class InvoiceRecalculationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceRecalculationScheduler.class);
    private final InvoiceRepository invoiceRepository;

    public InvoiceRecalculationScheduler(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void recalculateInvoiceTotals() {
        logger.info("Iniciando recálculo de totales de facturas");
        
        List<Invoice> invoices = invoiceRepository.findAll();
        
        for (Invoice invoice : invoices) {
            invoiceRepository.save(invoice);
        }
        
        logger.info("Recálculo completado. {} facturas procesadas", invoices.size());
    }
}
