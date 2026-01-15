package com.quotation.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class QuotationItem {
    private Long id;
    private Long quotationId;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
