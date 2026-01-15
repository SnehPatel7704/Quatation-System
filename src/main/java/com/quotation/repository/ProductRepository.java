package com.quotation.repository;

import com.quotation.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setBasePrice(rs.getBigDecimal("base_price"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return product;
    };

    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products", productRowMapper);
    }

    public Optional<Product> findById(Long id) {
        List<Product> products = jdbcTemplate.query(
            "SELECT * FROM products WHERE id = ?", productRowMapper, id);
        return products.isEmpty() ? Optional.empty() : Optional.of(products.get(0));
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            jdbcTemplate.update(
                "INSERT INTO products (name, description, base_price) VALUES (?, ?, ?)",
                product.getName(), product.getDescription(), product.getBasePrice());
        } else {
            jdbcTemplate.update(
                "UPDATE products SET name=?, description=?, base_price=? WHERE id=?",
                product.getName(), product.getDescription(), product.getBasePrice(), product.getId());
        }
        return product;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM products WHERE id = ?", id);
    }
}
