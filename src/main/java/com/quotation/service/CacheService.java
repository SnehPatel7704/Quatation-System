package com.quotation.service;

import com.quotation.model.Company;
import com.quotation.model.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheService {

    private final JdbcTemplate jdbcTemplate;

    public CacheService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Get all companies with caching
     * Cache expires after 5 minutes (configured in CacheConfig)
     */
    @Cacheable(value = "companies")
    public List<Company> getCachedCompanies() {
        return jdbcTemplate.query(
            "SELECT * FROM companies ORDER BY name",
            (rs, rowNum) -> {
                Company company = new Company();
                company.setId(rs.getLong("id"));
                company.setName(rs.getString("name"));
                company.setEmail(rs.getString("email"));
                company.setPhone(rs.getString("phone"));
                company.setAddress(rs.getString("address"));
                company.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return company;
            }
        );
    }

    /**
     * Get all products with caching
     * Cache expires after 5 minutes (configured in CacheConfig)
     */
    @Cacheable(value = "products")
    public List<Product> getCachedProducts() {
        return jdbcTemplate.query(
            "SELECT * FROM products ORDER BY name",
            (rs, rowNum) -> {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setBasePrice(rs.getBigDecimal("base_price"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return product;
            }
        );
    }

    /**
     * Invalidate company cache
     * Call this when companies are created, updated, or deleted
     */
    @CacheEvict(value = "companies", allEntries = true)
    public void invalidateCompanyCache() {
        // Cache will be cleared automatically by Spring
    }

    /**
     * Invalidate product cache
     * Call this when products are created, updated, or deleted
     */
    @CacheEvict(value = "products", allEntries = true)
    public void invalidateProductCache() {
        // Cache will be cleared automatically by Spring
    }
}
