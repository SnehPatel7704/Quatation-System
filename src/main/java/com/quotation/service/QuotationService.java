package com.quotation.service;

import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import com.quotation.model.Company;
import com.quotation.model.User;
import com.quotation.repository.QuotationRepository;
import com.quotation.repository.QuotationItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EmailService emailService;
    private final PDFService pdfService;

    public QuotationService(QuotationRepository quotationRepository,
                           QuotationItemRepository quotationItemRepository,
                           JdbcTemplate jdbcTemplate,
                           EmailService emailService,
                           PDFService pdfService) {
        this.quotationRepository = quotationRepository;
        this.quotationItemRepository = quotationItemRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
        this.pdfService = pdfService;
    }

    @Transactional
    public Quotation createQuotation(Quotation quotation, List<QuotationItem> items) {
        quotation.setQuotationNumber("QT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // Set status based on whether it's explicitly set or default behavior
        // If status is not set, use default logic:
        // - PENDING_APPROVAL for new quotations (not revisions)
        // - DRAFT for revisions (created in createRevision method)
        if (quotation.getStatus() == null) {
            if (quotation.getParentQuotationId() == null) {
                quotation.setStatus(Quotation.QuotationStatus.PENDING_APPROVAL);
            } else {
                quotation.setStatus(Quotation.QuotationStatus.DRAFT);
            }
        }
        // If status is explicitly set (e.g., DRAFT from frontend), keep it
        
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
    public Quotation approveQuotation(Long quotationId, Long approvedBy) {
        Quotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
        
        // Validate status transition
        if (quotation.getStatus() != Quotation.QuotationStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Only quotations with PENDING_APPROVAL status can be approved");
        }
        
        quotation.setStatus(Quotation.QuotationStatus.APPROVED);
        quotation.setApprovedBy(approvedBy);
        quotationRepository.save(quotation);
        
        // Send email notification to creator
        try {
            User creator = getUserById(quotation.getCreatedBy());
            if (creator != null && creator.getEmail() != null) {
                emailService.sendApprovalNotification(quotation, creator);
            }
        } catch (Exception e) {
            // Log error but don't fail the approval operation
            System.err.println("Failed to send approval notification: " + e.getMessage());
        }
        
        return quotation;
    }

    @Transactional
    public Quotation rejectQuotation(Long quotationId, String rejectionReason, Long rejectedBy) {
        Quotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
        
        // Validate status transition
        if (quotation.getStatus() != Quotation.QuotationStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Only quotations with PENDING_APPROVAL status can be rejected");
        }
        
        // Validate rejection reason
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason is required");
        }
        
        // Update original quotation to REJECTED status
        quotation.setStatus(Quotation.QuotationStatus.REJECTED);
        quotation.setRejectionReason(rejectionReason);
        quotationRepository.save(quotation);
        
        // Create a new revision with DRAFT status
        Quotation revision = createRevision(quotation);
        
        // Send email notification to creator with revision link
        try {
            User creator = getUserById(quotation.getCreatedBy());
            if (creator != null && creator.getEmail() != null) {
                emailService.sendRejectionNotification(quotation, creator, rejectionReason, revision.getId());
            }
        } catch (Exception e) {
            // Log error but don't fail the rejection operation
            System.err.println("Failed to send rejection notification: " + e.getMessage());
        }
        
        return revision;
    }

    @Transactional
    public Quotation createRevision(Quotation originalQuotation) {
        // Create new quotation as revision
        Quotation revision = new Quotation();
        revision.setQuotationNumber("QT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        revision.setCompanyId(originalQuotation.getCompanyId());
        revision.setTemplateId(originalQuotation.getTemplateId());
        revision.setStatus(Quotation.QuotationStatus.DRAFT);
        revision.setTotalAmount(originalQuotation.getTotalAmount());
        revision.setCreatedBy(originalQuotation.getCreatedBy());
        revision.setFollowUpDate(originalQuotation.getFollowUpDate());
        revision.setRevisionNumber(originalQuotation.getRevisionNumber() + 1);
        revision.setParentQuotationId(originalQuotation.getId());
        
        // Save the revision
        Long revisionId = quotationRepository.save(revision);
        revision.setId(revisionId);
        
        // Copy all items from original quotation
        List<QuotationItem> originalItems = getQuotationItems(originalQuotation.getId());
        for (QuotationItem originalItem : originalItems) {
            QuotationItem newItem = new QuotationItem();
            newItem.setQuotationId(revisionId);
            newItem.setProductId(originalItem.getProductId());
            newItem.setQuantity(originalItem.getQuantity());
            newItem.setUnitPrice(originalItem.getUnitPrice());
            newItem.setTotalPrice(originalItem.getTotalPrice());
            
            jdbcTemplate.update(
                "INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)",
                newItem.getQuotationId(), newItem.getProductId(), newItem.getQuantity(), 
                newItem.getUnitPrice(), newItem.getTotalPrice()
            );
        }
        
        return revision;
    }

    private List<QuotationItem> getQuotationItems(Long quotationId) {
        return jdbcTemplate.query(
            "SELECT * FROM quotation_items WHERE quotation_id = ?",
            (rs, rowNum) -> {
                QuotationItem item = new QuotationItem();
                item.setId(rs.getLong("id"));
                item.setQuotationId(rs.getLong("quotation_id"));
                item.setProductId(rs.getLong("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));
                item.setTotalPrice(rs.getBigDecimal("total_price"));
                return item;
            },
            quotationId
        );
    }

    @Transactional
    public void sendToClient(Long quotationId, String clientEmail) {
        Quotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
        
        // Validate status transition - only APPROVED quotations can be sent
        if (quotation.getStatus() != Quotation.QuotationStatus.APPROVED) {
            throw new IllegalStateException("Only quotations with APPROVED status can be sent to clients");
        }
        
        quotation.setStatus(Quotation.QuotationStatus.SENT);
        quotationRepository.save(quotation);
        
        emailService.sendQuotationToClient(quotation, clientEmail);
    }

    public List<Quotation> getAllQuotations() {
        List<Quotation> quotations = quotationRepository.findAll();
        
        // Populate items for each quotation
        for (Quotation quotation : quotations) {
            List<QuotationItem> items = quotationItemRepository.findByQuotationId(quotation.getId());
            quotation.setItems(items);
        }
        
        return quotations;
    }

    public Optional<Quotation> getQuotationById(Long id) {
        Optional<Quotation> quotationOpt = quotationRepository.findById(id);
        
        // Populate items if quotation exists
        quotationOpt.ifPresent(quotation -> {
            List<QuotationItem> items = quotationItemRepository.findByQuotationId(id);
            quotation.setItems(items);
        });
        
        return quotationOpt;
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

    @Transactional
    public Quotation submitForApproval(Long quotationId) {
        Quotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
        
        // Validate status transition - only DRAFT quotations can be submitted
        if (quotation.getStatus() != Quotation.QuotationStatus.DRAFT) {
            throw new IllegalStateException("Only quotations with DRAFT status can be submitted for approval");
        }
        
        quotation.setStatus(Quotation.QuotationStatus.PENDING_APPROVAL);
        quotationRepository.save(quotation);
        
        return quotation;
    }

    public void deleteQuotation(Long id) {
        jdbcTemplate.update("DELETE FROM quotations WHERE id = ?", id);
    }

    public List<Quotation> getRevisionHistory(Long quotationId) {
        return quotationRepository.findByParentQuotationId(quotationId);
    }


    public List<Quotation> getUpcomingFollowups(LocalDate startDate, LocalDate endDate) {
        // Validate date range
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        // Query quotations with follow-up dates in the specified range
        List<Quotation> quotations = quotationRepository.findByFollowUpDateBetween(startDate, endDate);

        // Populate company names for each quotation
        for (Quotation quotation : quotations) {
            try {
                String companyName = jdbcTemplate.queryForObject(
                    "SELECT name FROM companies WHERE id = ?",
                    String.class,
                    quotation.getCompanyId()
                );
                quotation.setCompanyName(companyName);
            } catch (Exception e) {
                // If company not found or error occurs, set a default value
                quotation.setCompanyName("Unknown Company");
            }
        }

        return quotations;
    }


    public byte[] generatePDF(Long quotationId) {
        // Get quotation
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));

        // Validate status - only APPROVED or SENT quotations can be exported
        if (quotation.getStatus() != Quotation.QuotationStatus.APPROVED &&
            quotation.getStatus() != Quotation.QuotationStatus.SENT) {
            throw new IllegalStateException("Only APPROVED or SENT quotations can be exported as PDF");
        }

        // Get quotation items
        List<QuotationItem> items = getQuotationItems(quotationId);

        // Get company information
        Company company = jdbcTemplate.queryForObject(
                "SELECT * FROM companies WHERE id = ?",
                (rs, rowNum) -> {
                    Company c = new Company();
                    c.setId(rs.getLong("id"));
                    c.setName(rs.getString("name"));
                    c.setEmail(rs.getString("email"));
                    c.setPhone(rs.getString("phone"));
                    c.setAddress(rs.getString("address"));
                    return c;
                },
                quotation.getCompanyId()
        );

        // Generate PDF
        return pdfService.generateQuotationPDF(quotation, items, company);
    }


    /**
     * Helper method to get user by ID
     */
    private User getUserById(Long userId) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                (rs, rowNum) -> {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(User.Role.valueOf(rs.getString("role")));
                    user.setEnabled(rs.getBoolean("enabled"));
                    return user;
                },
                userId
            );
        } catch (Exception e) {
            System.err.println("Failed to fetch user: " + e.getMessage());
            return null;
        }
    }



}
