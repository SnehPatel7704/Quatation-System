package com.quotation.controller;

import com.quotation.dto.QuotationRequest;
import com.quotation.model.Quotation;
import com.quotation.service.QuotationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<List<Quotation>> listQuotations() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<Quotation> getQuotation(@PathVariable Long id) {
        return quotationService.getQuotationById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<Quotation> createQuotation(@RequestBody QuotationRequest request,
                                                     Authentication auth) {
        Quotation quotation = quotationService.createQuotation(request.getQuotation(), request.getItems());
        return ResponseEntity.ok(quotation);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<Quotation> updateQuotation(@PathVariable Long id,
                                                     @RequestBody QuotationRequest request) {
        Quotation quotation = quotationService.updateQuotation(id, request.getQuotation(), request.getItems());
        return ResponseEntity.ok(quotation);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<?> approveQuotation(@PathVariable Long id,
                                             @RequestParam String adminEmail,
                                             @RequestParam String clientEmail,
                                             Authentication auth) {
        quotationService.approveQuotation(id, 1L, adminEmail, clientEmail);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/send")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<?> sendToClient(@PathVariable Long id, @RequestParam String clientEmail) {
        quotationService.sendToClient(id, clientEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<?> deleteQuotation(@PathVariable Long id) {
        quotationService.deleteQuotation(id);
        return ResponseEntity.ok().build();
    }
}
