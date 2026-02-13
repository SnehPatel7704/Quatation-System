package com.quotation.repository;

import com.quotation.model.QuotationItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QuotationItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getItemsCall;
    private final SimpleJdbcCall createItemCall;
    private final SimpleJdbcCall updateItemCall;
    private final SimpleJdbcCall deleteItemsCall;

    public QuotationItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        
        // Initialize stored procedure calls
        this.getItemsCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("sp_get_quotation_items")
            .returningResultSet("items", quotationItemRowMapper);
            
        this.createItemCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("sp_create_quotation_item")
            .declareParameters(
                new SqlParameter("p_quotation_id", Types.BIGINT),
                new SqlParameter("p_product_id", Types.BIGINT),
                new SqlParameter("p_quantity", Types.INTEGER),
                new SqlParameter("p_unit_price", Types.DECIMAL),
                new SqlParameter("p_total_price", Types.DECIMAL),
                new SqlOutParameter("p_item_id", Types.BIGINT)
            );
            
        this.updateItemCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("sp_update_quotation_item")
            .declareParameters(
                new SqlParameter("p_item_id", Types.BIGINT),
                new SqlParameter("p_product_id", Types.BIGINT),
                new SqlParameter("p_quantity", Types.INTEGER),
                new SqlParameter("p_unit_price", Types.DECIMAL),
                new SqlParameter("p_total_price", Types.DECIMAL)
            );
            
        this.deleteItemsCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("sp_delete_quotation_items")
            .declareParameters(
                new SqlParameter("p_quotation_id", Types.BIGINT)
            );
    }

    private final RowMapper<QuotationItem> quotationItemRowMapper = (rs, rowNum) -> {
        QuotationItem item = new QuotationItem();
        item.setId(rs.getLong("id"));
        item.setQuotationId(rs.getLong("quotation_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setTotalPrice(rs.getBigDecimal("total_price"));
        
        // Set product name if available from the join
        try {
            item.setProductName(rs.getString("product_name"));
        } catch (Exception e) {
            // Column not available
        }
        
        return item;
    };

    /**
     * Find all items for a quotation using stored procedure
     */
    public List<QuotationItem> findByQuotationId(Long quotationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_quotation_id", quotationId);
        
        Map<String, Object> result = getItemsCall.execute(params);
        @SuppressWarnings("unchecked")
        List<QuotationItem> items = (List<QuotationItem>) result.get("items");
        
        return items != null ? items : List.of();
    }

    /**
     * Create a new quotation item using stored procedure
     */
    public Long save(QuotationItem item) {
        if (item.getId() == null) {
            // Create new item
            Map<String, Object> params = new HashMap<>();
            params.put("p_quotation_id", item.getQuotationId());
            params.put("p_product_id", item.getProductId());
            params.put("p_quantity", item.getQuantity());
            params.put("p_unit_price", item.getUnitPrice());
            params.put("p_total_price", item.getTotalPrice());
            
            Map<String, Object> result = createItemCall.execute(params);
            return ((Number) result.get("p_item_id")).longValue();
        } else {
            // Update existing item
            Map<String, Object> params = new HashMap<>();
            params.put("p_item_id", item.getId());
            params.put("p_product_id", item.getProductId());
            params.put("p_quantity", item.getQuantity());
            params.put("p_unit_price", item.getUnitPrice());
            params.put("p_total_price", item.getTotalPrice());
            
            updateItemCall.execute(params);
            return item.getId();
        }
    }

    /**
     * Delete all items for a quotation using stored procedure
     */
    public void deleteByQuotationId(Long quotationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_quotation_id", quotationId);
        
        deleteItemsCall.execute(params);
    }
}
