package com.quotation.dto;

import jakarta.validation.constraints.NotNull;

public class ApprovalRequest {
    @NotNull(message = "Quotation ID is required")
    private Long quotationId;
    
    @NotNull(message = "Approver ID is required")
    private Long approvedBy;

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
}
