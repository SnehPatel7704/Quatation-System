package com.quotation.service;

import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import com.quotation.repository.QuotationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.UUID;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EmailService emailService;

    public QuotationService(QuotationRepository quotationRepository, 
                           JdbcTemplate jdbcTemplate,
                           EmailService emailService) {
        this.quotationRepository = quotationRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
    }

    @Transactional
    public Quotation createQuotation(Quotation quotation, List<QuotationItem> items) {
        quotation.setQuotationNumber("QT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        quotation.setStatus(Quotation.QuotationStatus.DRAFT);
        
        BigDecimal total = items.stream()
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        quotation.setTotalAmount(total);
        
        Long quotationId = quotationRepository.save(quotation);
        quotation.setId(quotationId);
        
        for (QuotationItem item : items) {
            item.setQuotationId(quotationId);
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            jdbcTemplate.update(
                "INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)",
                item.getQuotationId(), item.getProductId(), item.getQuantity(), 
                item.getUnitPrice(), item.getTotalPrice()
            );
        }
        
        return quotation;
    }

    @Transactional
    public void approveQuotation(Long quotationId, Long approvedBy, String adminEmail, String clientEmail) {
        Quotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
        
        quotation.setStatus(Quotation.QuotationStatus.APPROVED);
        quotation.setApprovedBy(approvedBy);
        quotationRepository.save(quotation);
        
        emailService.sendQuotationToAdmin(quotation, adminEmail);
    }

    @Transactional
    public void sendToClient(Long quotationId, String clientEmail) {
        Quotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
        
        quotation.setStatus(Quotation.QuotationStatus.SENT);
        quotationRepository.save(quotation);
        
        emailService.sendQuotationToClient(quotation, clientEmail);
    }

    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }

    public Optional<Quotation> getQuotationById(Long id) {
        return quotationRepository.findById(id);
    }

    @Transactional
    public Quotation updateQuotation(Long id, Quotation quotation, List<QuotationItem> items) {
        quotation.setId(id);
        
        jdbcTemplate.update("DELETE FROM quotation_items WHERE quotation_id = ?", id);
        
        BigDecimal total = items.stream()
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        quotation.setTotalAmount(total);
        
        quotationRepository.save(quotation);
        
        for (QuotationItem item : items) {
            item.setQuotationId(id);
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            jdbcTemplate.update(
                "INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)",
                item.getQuotationId(), item.getProductId(), item.getQuantity(), 
                item.getUnitPrice(), item.getTotalPrice()
            );
        }
        
        return quotation;
    }

    public void deleteQuotation(Long id) {
        jdbcTemplate.update("DELETE FROM quotations WHERE id = ?", id);
    }
}
