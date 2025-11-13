package com.contfiable.controller.invoice;

import com.contfiable.dto.invoice.InvoiceCreateRequest;
import com.contfiable.dto.invoice.InvoiceResponse;
import com.contfiable.dto.invoice.InvoiceUpdateRequest;
import com.contfiable.service.invoice.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceCreateRequest request) {
        InvoiceResponse response = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene una factura por ID.
     * Requiere autenticación JWT en el header Authorization: Bearer {token}
     * Solo devuelve facturas del usuario autenticado.
     */
    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long invoiceId) {
        InvoiceResponse response = invoiceService.getInvoice(invoiceId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todas las facturas del usuario autenticado.
     * Requiere autenticación JWT en el header Authorization: Bearer {token}
     * Solo devuelve facturas del usuario autenticado.
     */
    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getInvoices() {
        List<InvoiceResponse> responses = invoiceService.getInvoices();
        return ResponseEntity.ok(responses);
    }

    /**
     * Actualiza una factura por ID.
     * Requiere autenticación JWT en el header Authorization: Bearer {token}
     * Solo permite actualizar facturas del usuario autenticado.
     */
    @PutMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponse> updateInvoice(
            @PathVariable Long invoiceId,
            @Valid @RequestBody InvoiceUpdateRequest request
    ) {
        InvoiceResponse response = invoiceService.updateInvoice(invoiceId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una factura por ID.
     * Requiere autenticación JWT en el header Authorization: Bearer {token}
     * Solo permite eliminar facturas del usuario autenticado.
     */
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.noContent().build();
    }
}

