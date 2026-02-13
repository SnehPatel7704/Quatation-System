-- Stored Procedures for Quotation Management System
-- This file contains all stored procedures and views for database operations

USE quotation_db;

-- Drop existing procedures if they exist
DROP PROCEDURE IF EXISTS sp_get_all_quotations;
DROP PROCEDURE IF EXISTS sp_get_quotation_by_id;
DROP PROCEDURE IF EXISTS sp_get_quotation_items;
DROP PROCEDURE IF EXISTS sp_create_quotation;
DROP PROCEDURE IF EXISTS sp_update_quotation;
DROP PROCEDURE IF EXISTS sp_delete_quotation;
DROP PROCEDURE IF EXISTS sp_create_quotation_item;
DROP PROCEDURE IF EXISTS sp_update_quotation_item;
DROP PROCEDURE IF EXISTS sp_delete_quotation_items;
DROP PROCEDURE IF EXISTS sp_get_all_companies;
DROP PROCEDURE IF EXISTS sp_get_all_products;

-- Drop existing views if they exist
DROP VIEW IF EXISTS v_quotation_details;
DROP VIEW IF EXISTS v_quotation_with_items;

-- ============================================================================
-- QUOTATION PROCEDURES
-- ============================================================================

-- Get all quotations
DELIMITER //
CREATE PROCEDURE sp_get_all_quotations()
BEGIN
    SELECT * FROM quotations ORDER BY created_at DESC;
END //
DELIMITER ;

-- Get quotation by ID
DELIMITER //
CREATE PROCEDURE sp_get_quotation_by_id(IN p_quotation_id BIGINT)
BEGIN
    SELECT * FROM quotations WHERE id = p_quotation_id;
END //
DELIMITER ;

-- Get quotation items by quotation ID
DELIMITER //
CREATE PROCEDURE sp_get_quotation_items(IN p_quotation_id BIGINT)
BEGIN
    SELECT 
        qi.*,
        p.name as product_name,
        p.description as product_description
    FROM quotation_items qi
    LEFT JOIN products p ON qi.product_id = p.id
    WHERE qi.quotation_id = p_quotation_id
    ORDER BY qi.id;
END //
DELIMITER ;

-- Create quotation
DELIMITER //
CREATE PROCEDURE sp_create_quotation(
    IN p_quotation_number VARCHAR(50),
    IN p_company_id BIGINT,
    IN p_template_id BIGINT,
    IN p_status VARCHAR(20),
    IN p_total_amount DECIMAL(12,2),
    IN p_created_by BIGINT,
    OUT p_quotation_id BIGINT
)
BEGIN
    INSERT INTO quotations (
        quotation_number, 
        company_id, 
        template_id, 
        status, 
        total_amount, 
        created_by
    ) VALUES (
        p_quotation_number,
        p_company_id,
        p_template_id,
        p_status,
        p_total_amount,
        p_created_by
    );
    
    SET p_quotation_id = LAST_INSERT_ID();
END //
DELIMITER ;

-- Update quotation
DELIMITER //
CREATE PROCEDURE sp_update_quotation(
    IN p_quotation_id BIGINT,
    IN p_status VARCHAR(20),
    IN p_total_amount DECIMAL(12,2),
    IN p_approved_by BIGINT
)
BEGIN
    UPDATE quotations 
    SET 
        status = p_status,
        total_amount = p_total_amount,
        approved_by = p_approved_by,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = p_quotation_id;
END //
DELIMITER ;

-- Delete quotation
DELIMITER //
CREATE PROCEDURE sp_delete_quotation(IN p_quotation_id BIGINT)
BEGIN
    -- Delete items first (foreign key constraint)
    DELETE FROM quotation_items WHERE quotation_id = p_quotation_id;
    
    -- Delete quotation
    DELETE FROM quotations WHERE id = p_quotation_id;
END //
DELIMITER ;

-- ============================================================================
-- QUOTATION ITEM PROCEDURES
-- ============================================================================

-- Create quotation item
DELIMITER //
CREATE PROCEDURE sp_create_quotation_item(
    IN p_quotation_id BIGINT,
    IN p_product_id BIGINT,
    IN p_quantity INT,
    IN p_unit_price DECIMAL(10,2),
    IN p_total_price DECIMAL(10,2),
    OUT p_item_id BIGINT
)
BEGIN
    INSERT INTO quotation_items (
        quotation_id,
        product_id,
        quantity,
        unit_price,
        total_price
    ) VALUES (
        p_quotation_id,
        p_product_id,
        p_quantity,
        p_unit_price,
        p_total_price
    );
    
    SET p_item_id = LAST_INSERT_ID();
END //
DELIMITER ;

-- Update quotation item
DELIMITER //
CREATE PROCEDURE sp_update_quotation_item(
    IN p_item_id BIGINT,
    IN p_product_id BIGINT,
    IN p_quantity INT,
    IN p_unit_price DECIMAL(10,2),
    IN p_total_price DECIMAL(10,2)
)
BEGIN
    UPDATE quotation_items 
    SET 
        product_id = p_product_id,
        quantity = p_quantity,
        unit_price = p_unit_price,
        total_price = p_total_price
    WHERE id = p_item_id;
END //
DELIMITER ;

-- Delete all items for a quotation
DELIMITER //
CREATE PROCEDURE sp_delete_quotation_items(IN p_quotation_id BIGINT)
BEGIN
    DELETE FROM quotation_items WHERE quotation_id = p_quotation_id;
END //
DELIMITER ;

-- ============================================================================
-- COMPANY AND PRODUCT PROCEDURES
-- ============================================================================

-- Get all companies
DELIMITER //
CREATE PROCEDURE sp_get_all_companies()
BEGIN
    SELECT * FROM companies ORDER BY name;
END //
DELIMITER ;

-- Get all products
DELIMITER //
CREATE PROCEDURE sp_get_all_products()
BEGIN
    SELECT * FROM products ORDER BY name;
END //
DELIMITER ;

-- ============================================================================
-- VIEWS
-- ============================================================================

-- View: Quotation with company details
CREATE VIEW v_quotation_details AS
SELECT 
    q.*,
    c.name as company_name_from_table,
    c.email as company_email_from_table,
    c.phone as company_phone_from_table,
    c.address as company_address_from_table,
    u1.username as creator_name,
    u2.username as approver_name
FROM quotations q
LEFT JOIN companies c ON q.company_id = c.id
LEFT JOIN users u1 ON q.created_by = u1.id
LEFT JOIN users u2 ON q.approved_by = u2.id;

-- View: Quotation with items
CREATE VIEW v_quotation_with_items AS
SELECT 
    q.id as quotation_id,
    q.quotation_number,
    q.company_id,
    q.status,
    q.total_amount,
    q.created_at,
    qi.id as item_id,
    qi.product_id,
    p.name as product_name,
    qi.quantity,
    qi.unit_price,
    qi.total_price
FROM quotations q
LEFT JOIN quotation_items qi ON q.id = qi.quotation_id
LEFT JOIN products p ON qi.product_id = p.id;

-- Display success message
SELECT 'Stored procedures and views created successfully!' as Status;

-- Show all procedures
SELECT 
    ROUTINE_NAME as 'Stored Procedure',
    ROUTINE_TYPE as 'Type'
FROM information_schema.ROUTINES
WHERE ROUTINE_SCHEMA = 'quotation_db'
AND ROUTINE_TYPE = 'PROCEDURE'
ORDER BY ROUTINE_NAME;

-- Show all views
SELECT 
    TABLE_NAME as 'View Name'
FROM information_schema.VIEWS
WHERE TABLE_SCHEMA = 'quotation_db'
ORDER BY TABLE_NAME;
