-- Migration Script: Add Category Column to Products Table
-- Feature: quotation-product-enhancements
-- Requirements: 1.1, 1.2
-- 
-- This migration adds a category column to the products table to support
-- product categorization and filtering

-- Add category column to products table
ALTER TABLE products 
ADD COLUMN category VARCHAR(100) NULL
COMMENT 'Product category (e.g., Office Supplies, Software, Hardware, Furniture, Consumables)';

-- Create index on category column for efficient filtering and search
CREATE INDEX idx_products_category ON products(category);

-- Verify the migration
-- Expected: category column should exist and be nullable
-- Expected: index should be created on category column
SELECT 
    COUNT(*) as total_products,
    COUNT(category) as products_with_category,
    COUNT(CASE WHEN category IS NULL THEN 1 END) as products_without_category
FROM products;
