package com.quotation.dto;

import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import lombok.Data;

import java.util.List;

@Data
public class QuotationRequest {
    private Quotation quotation;
    private List<QuotationItem> items;
}
