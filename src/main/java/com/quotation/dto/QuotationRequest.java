package com.quotation.dto;

import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;

import java.util.List;

public class QuotationRequest {
    private Quotation quotation;
    private List<QuotationItem> items;

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public List<QuotationItem> getItems() {
        return items;
    }

    public void setItems(List<QuotationItem> items) {
        this.items = items;
    }
}
