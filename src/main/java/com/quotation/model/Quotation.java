package com.quotation.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Quotation {
    private Long id;
    private String quotationNumber;
    private Long companyId;
    private Long templateId;
    private QuotationStatus status;
    private BigDecimal totalAmount;
    private Long createdBy;
    private Long approvedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QuotationItem> items;
    
    public enum QuotationStatus {
        DRAFT, PENDING_APPROVAL, APPROVED, SENT, REJECTED
    }
}
