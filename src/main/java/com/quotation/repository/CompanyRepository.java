package com.quotation.repository;

import com.quotation.model.Company;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    public CompanyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Company> companyRowMapper = (rs, rowNum) -> {
        Company company = new Company();
        company.setId(rs.getLong("id"));
        company.setName(rs.getString("name"));
        company.setEmail(rs.getString("email"));
        company.setPhone(rs.getString("phone"));
        company.setAddress(rs.getString("address"));
        company.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return company;
    };

    public List<Company> findAll() {
        return jdbcTemplate.query("SELECT * FROM companies", companyRowMapper);
    }

    public Optional<Company> findById(Long id) {
        List<Company> companies = jdbcTemplate.query(
            "SELECT * FROM companies WHERE id = ?", companyRowMapper, id);
        return companies.isEmpty() ? Optional.empty() : Optional.of(companies.get(0));
    }

    public Company save(Company company) {
        if (company.getId() == null) {
            jdbcTemplate.update(
                "INSERT INTO companies (name, email, phone, address) VALUES (?, ?, ?, ?)",
                company.getName(), company.getEmail(), company.getPhone(), company.getAddress());
        } else {
            jdbcTemplate.update(
                "UPDATE companies SET name=?, email=?, phone=?, address=? WHERE id=?",
                company.getName(), company.getEmail(), company.getPhone(), 
                company.getAddress(), company.getId());
        }
        return company;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM companies WHERE id = ?", id);
    }
}
