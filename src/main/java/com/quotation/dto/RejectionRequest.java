package com.quotation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RejectionRequest {
    @NotNull(message = "Quotation ID is required")
    private Long quotationId;
    
    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;
    
    @NotNull(message = "Rejector ID is required")
    private Long rejectedBy;

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Long getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(Long rejectedBy) {
        this.rejectedBy = rejectedBy;
    }
}
