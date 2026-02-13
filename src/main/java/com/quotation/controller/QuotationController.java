package com.quotation.controller;

import com.quotation.dto.QuotationRequest;
import com.quotation.model.Quotation;
import com.quotation.service.QuotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
@Tag(name = "Quotations", description = "Quotation management endpoints")
@SecurityRequirement(name = "bearer-jwt")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    @Operation(summary = "Get all quotations", description = "Retrieve a list of all quotations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved quotations",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Quotation.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<List<Quotation>> listQuotations() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    @Operation(summary = "Get quotation by ID", description = "Retrieve a specific quotation by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved quotation",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Quotation.class))),
        @ApiResponse(responseCode = "404", description = "Quotation not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Quotation> getQuotation(
            @Parameter(description = "ID of the quotation to retrieve", required = true)
            @PathVariable Long id) {
        return quotationService.getQuotationById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @Operation(summary = "Create new quotation", description = "Create a new quotation with items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quotation created successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Quotation.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Quotation> createQuotation(
            @Parameter(description = "Quotation data with items", required = true)
            @RequestBody QuotationRequest request,
            Authentication auth) {
        Quotation quotation = quotationService.createQuotation(request.getQuotation(), request.getItems());
        return ResponseEntity.ok(quotation);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @Operation(summary = "Update quotation", description = "Update an existing quotation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quotation updated successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Quotation.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Quotation not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Quotation> updateQuotation(
            @Parameter(description = "ID of the quotation to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated quotation data", required = true)
            @RequestBody QuotationRequest request) {
        Quotation quotation = quotationService.updateQuotation(id, request.getQuotation(), request.getItems());
        return ResponseEntity.ok(quotation);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<Quotation> approveQuotation(@PathVariable Long id, Authentication auth) {
        // Extract user ID from authentication
        // For now, using a placeholder - this should be extracted from the auth object
        Long approvedBy = 1L; // TODO: Extract from auth.getPrincipal()
        
        Quotation quotation = quotationService.approveQuotation(id, approvedBy);
        return ResponseEntity.ok(quotation);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<Quotation> rejectQuotation(@PathVariable Long id, 
                                                     @RequestParam String rejectionReason,
                                                     Authentication auth) {
        // Extract user ID from authentication
        // For now, using a placeholder - this should be extracted from the auth object
        Long rejectedBy = 1L; // TODO: Extract from auth.getPrincipal()
        
        Quotation revision = quotationService.rejectQuotation(id, rejectionReason, rejectedBy);
        return ResponseEntity.ok(revision);
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    @Operation(summary = "Submit draft for approval", description = "Submit a DRAFT quotation for approval")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quotation submitted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status - only DRAFT quotations can be submitted"),
        @ApiResponse(responseCode = "404", description = "Quotation not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Quotation> submitForApproval(
            @Parameter(description = "ID of the quotation to submit", required = true)
            @PathVariable Long id) {
        Quotation quotation = quotationService.submitForApproval(id);
        return ResponseEntity.ok(quotation);
    }

    @GetMapping("/{id}/revisions")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<List<Quotation>> getRevisions(@PathVariable Long id) {
        List<Quotation> revisions = quotationService.getRevisionHistory(id);
        return ResponseEntity.ok(revisions);
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


    @GetMapping("/upcoming-followups")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<List<Quotation>> getUpcomingFollowups(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        java.time.LocalDate start = java.time.LocalDate.parse(startDate);
        java.time.LocalDate end = java.time.LocalDate.parse(endDate);
        List<Quotation> quotations = quotationService.getUpcomingFollowups(start, end);
        return ResponseEntity.ok(quotations);
    }


    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'USER')")
    public ResponseEntity<byte[]> exportPDF(@PathVariable Long id) {
        byte[] pdfBytes = quotationService.generatePDF(id);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=quotation-" + id + ".pdf")
                .body(pdfBytes);
    }


}
