package com.quotation.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuotationTemplate {
    private Long id;
    private String name;
    private String templateData;
    private Long createdBy;
    private LocalDateTime createdAt;
}
