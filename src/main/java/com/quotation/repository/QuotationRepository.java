package com.quotation.repository;

import com.quotation.model.Quotation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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

    public Long save(Quotation quotation) {
        if (quotation.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO quotations (quotation_number, company_id, template_id, status, total_amount, created_by) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, quotation.getQuotationNumber());
                ps.setLong(2, quotation.getCompanyId());
                ps.setObject(3, quotation.getTemplateId());
                ps.setString(4, quotation.getStatus().name());
                ps.setBigDecimal(5, quotation.getTotalAmount());
                ps.setLong(6, quotation.getCreatedBy());
                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        } else {
            jdbcTemplate.update(
                "UPDATE quotations SET status=?, total_amount=?, approved_by=? WHERE id=?",
                quotation.getStatus().name(), quotation.getTotalAmount(), 
                quotation.getApprovedBy(), quotation.getId());
            return quotation.getId();
        }
    }
}
