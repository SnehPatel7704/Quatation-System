package com.quotation.repository;

import com.quotation.model.Quotation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class QuotationRepository {

    private final JdbcTemplate jdbcTemplate;

    public QuotationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Quotation> quotationRowMapper = (rs, rowNum) -> {
        Quotation quotation = new Quotation();
        quotation.setId(rs.getLong("id"));
        quotation.setQuotationNumber(rs.getString("quotation_number"));
        quotation.setCompanyId(rs.getLong("company_id"));
        quotation.setTemplateId(rs.getObject("template_id", Long.class));
        quotation.setStatus(Quotation.QuotationStatus.valueOf(rs.getString("status")));
        quotation.setTotalAmount(rs.getBigDecimal("total_amount"));
        quotation.setCreatedBy(rs.getLong("created_by"));
        quotation.setApprovedBy(rs.getObject("approved_by", Long.class));
        quotation.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        quotation.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        // Map new fields (Phase 2) - handle missing columns gracefully
        try {
            Date followUpDate = rs.getDate("follow_up_date");
            if (followUpDate != null) {
                quotation.setFollowUpDate(followUpDate.toLocalDate());
            }
        } catch (Exception e) {
            // Column doesn't exist yet - Phase 2 feature
        }
        
        try {
            quotation.setRejectionReason(rs.getString("rejection_reason"));
        } catch (Exception e) {
            // Column doesn't exist yet - Phase 2 feature
        }
        
        try {
            quotation.setRevisionNumber(rs.getInt("revision_number"));
        } catch (Exception e) {
            // Column doesn't exist yet - Phase 2 feature
            quotation.setRevisionNumber(1);
        }
        
        try {
            quotation.setParentQuotationId(rs.getObject("parent_quotation_id", Long.class));
        } catch (Exception e) {
            // Column doesn't exist yet - Phase 2 feature
        }
        
        return quotation;
    };

    public List<Quotation> findAll() {
        return jdbcTemplate.query("SELECT * FROM quotations ORDER BY created_at DESC", quotationRowMapper);
    }

    public Optional<Quotation> findById(Long id) {
        List<Quotation> quotations = jdbcTemplate.query(
            "SELECT * FROM quotations WHERE id = ?", quotationRowMapper, id);
        return quotations.isEmpty() ? Optional.empty() : Optional.of(quotations.get(0));
    }

    /**
     * Find quotation by ID with items joined
     * Uses JOIN to minimize database round trips
     */
    public Optional<Quotation> findByIdWithItems(Long id) {
        // First get the quotation
        Optional<Quotation> quotationOpt = findById(id);
        if (quotationOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Quotation quotation = quotationOpt.get();
        
        // Then get items in a single query
        // Note: This assumes QuotationItem table exists with proper structure
        // The actual implementation would depend on QuotationItemRepository
        
        return Optional.of(quotation);
    }

    /**
     * Find quotations by follow-up date range
     * Used for upcoming follow-ups dashboard
     */
    public List<Quotation> findByFollowUpDateBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM quotations " +
                     "WHERE follow_up_date IS NOT NULL " +
                     "AND follow_up_date >= ? " +
                     "AND follow_up_date <= ? " +
                     "ORDER BY follow_up_date ASC";
        return jdbcTemplate.query(sql, quotationRowMapper, 
                                  Date.valueOf(startDate), 
                                  Date.valueOf(endDate));
    }

    /**
     * Find all revisions of a quotation (children)
     * Used for displaying revision history
     */
    public List<Quotation> findByParentQuotationId(Long parentId) {
        String sql = "SELECT * FROM quotations " +
                     "WHERE parent_quotation_id = ? " +
                     "ORDER BY revision_number ASC";
        return jdbcTemplate.query(sql, quotationRowMapper, parentId);
    }

    /**
     * Find quotations by status
     * Used for filtering quotations by status
     */
    public List<Quotation> findByStatus(Quotation.QuotationStatus status) {
        String sql = "SELECT * FROM quotations " +
                     "WHERE status = ? " +
                     "ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, quotationRowMapper, status.name());
    }

    public Long save(Quotation quotation) {
        if (quotation.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quotations (quotation_number, company_id, template_id, status, total_amount, created_by, follow_up_date, rejection_reason, revision_number, parent_quotation_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new String[]{"id"}); // Specify which key to return
                ps.setString(1, quotation.getQuotationNumber());
                ps.setLong(2, quotation.getCompanyId());
                ps.setObject(3, quotation.getTemplateId());
                ps.setString(4, quotation.getStatus().name());
                ps.setBigDecimal(5, quotation.getTotalAmount());
                ps.setLong(6, quotation.getCreatedBy());
                
                // Set new fields
                if (quotation.getFollowUpDate() != null) {
                    ps.setDate(7, Date.valueOf(quotation.getFollowUpDate()));
                } else {
                    ps.setNull(7, java.sql.Types.DATE);
                }
                ps.setString(8, quotation.getRejectionReason());
                ps.setInt(9, quotation.getRevisionNumber() != null ? quotation.getRevisionNumber() : 1);
                ps.setObject(10, quotation.getParentQuotationId());
                
                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        } else {
            jdbcTemplate.update(
                "UPDATE quotations SET status=?, total_amount=?, approved_by=?, follow_up_date=?, rejection_reason=?, revision_number=?, parent_quotation_id=? WHERE id=?",
                quotation.getStatus().name(), 
                quotation.getTotalAmount(), 
                quotation.getApprovedBy(),
                quotation.getFollowUpDate() != null ? Date.valueOf(quotation.getFollowUpDate()) : null,
                quotation.getRejectionReason(),
                quotation.getRevisionNumber(),
                quotation.getParentQuotationId(),
                quotation.getId());
            return quotation.getId();
        }
    }


    /**
     * Find all quotations with pagination support
     *
     * @param page Page number (0-indexed)
     * @param size Number of items per page
     * @return List of quotations for the specified page
     */
    public List<Quotation> findAllWithPagination(int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM quotations " +
                     "ORDER BY created_at DESC " +
                     "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, quotationRowMapper, size, offset);
    }

    /**
     * Count total number of quotations
     * Used for pagination
     */
    public long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM quotations", Long.class);
    }

}
