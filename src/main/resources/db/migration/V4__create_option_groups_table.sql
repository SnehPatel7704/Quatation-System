-- Migration Script: Create Option Groups Table
-- Feature: quotation-product-enhancements
-- Requirements: 4.1, 4.2, 8.1
-- 
-- This migration creates the option_groups table to support multiple
-- product option groups within quotations, allowing clients to choose
-- between alternative packages

-- Create option_groups table
CREATE TABLE option_groups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quotation_id BIGINT NOT NULL
        COMMENT 'Reference to the parent quotation',
    name VARCHAR(255) NOT NULL
        COMMENT 'Name of the option group (e.g., "Basic Package", "Premium Package")',
    display_order INT NOT NULL DEFAULT 0
        COMMENT 'Order in which option groups should be displayed',
    is_selected BOOLEAN NOT NULL DEFAULT FALSE
        COMMENT 'Whether this option group is selected by the client',
    subtotal DECIMAL(15, 2) NOT NULL DEFAULT 0.00
        COMMENT 'Sum of all item prices in this option group',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT 'Timestamp when the option group was created',
    
    -- Foreign key constraint to quotations table with CASCADE delete
    CONSTRAINT fk_option_groups_quotation 
        FOREIGN KEY (quotation_id) 
        REFERENCES quotations(id) 
        ON DELETE CASCADE
);

-- Create index on quotation_id for efficient lookup of option groups by quotation
CREATE INDEX idx_option_groups_quotation ON option_groups(quotation_id);

-- Create composite index on (quotation_id, is_selected) for efficient selection queries
CREATE INDEX idx_option_groups_selected ON option_groups(quotation_id, is_selected);

-- Verify the migration
-- Expected: option_groups table should exist with all columns
-- Expected: Indexes should be created
-- Expected: Foreign key constraint should exist
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'option_groups'
ORDER BY ORDINAL_POSITION;

