package com.contfiable.controller.invoice;

import com.contfiable.dto.invoice.InvoiceCreateRequest;
import com.contfiable.dto.invoice.InvoiceResponse;
import com.contfiable.dto.invoice.InvoiceSummaryResponse;
import com.contfiable.dto.invoice.InvoiceUpdateRequest;
import com.contfiable.service.invoice.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ObjectMapper objectMapper;

    public InvoiceController(InvoiceService invoiceService, ObjectMapper objectMapper) {
        this.invoiceService = invoiceService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvoiceResponse> createInvoice(
            @RequestPart("data") String dataJson,
            @RequestPart(value = "pdfFile", required = false) MultipartFile pdfFile,
            @RequestPart(value = "xmlFile", required = false) MultipartFile xmlFile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "articleImages", required = false) List<MultipartFile> articleImages
    ) throws Exception {
        InvoiceCreateRequest request = objectMapper.readValue(dataJson, InvoiceCreateRequest.class);
        InvoiceResponse response = invoiceService.createInvoice(request, pdfFile, xmlFile, imageFile, articleImages);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getInvoices() {
        List<InvoiceResponse> responses = invoiceService.getInvoices();
        return ResponseEntity.ok(responses);
    }

    // ⚠️ IMPORTANTE: /summary DEBE ir ANTES de /{invoiceId}
    @GetMapping("/summary")
    public ResponseEntity<List<InvoiceSummaryResponse>> getInvoicesSummary() {
        List<InvoiceSummaryResponse> responses = invoiceService.getInvoicesSummary();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long invoiceId) {
        InvoiceResponse response = invoiceService.getInvoice(invoiceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponse> updateInvoice(
            @PathVariable Long invoiceId,
            @Valid @RequestBody InvoiceUpdateRequest request
    ) {
        InvoiceResponse response = invoiceService.updateInvoice(invoiceId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.noContent().build();
    }
}