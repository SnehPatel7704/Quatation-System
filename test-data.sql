-- Test Data for Quotation Management System
-- This script populates the database with sample data for testing

-- Clear existing data (optional - comment out if you want to keep existing data)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE quotation_items;
TRUNCATE TABLE quotations;
TRUNCATE TABLE products;
TRUNCATE TABLE companies;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- Insert Users
-- Password for all users: "password123" (BCrypt hashed)
-- Hash generated with: new BCryptPasswordEncoder().encode("password123")
INSERT INTO users (username, password, email, role, enabled) VALUES
('admin', '$2a$10$75wTrGA8RTZSlkoFxIfS8e.UbqmUlezIPafiJiK9tuUWpvNBwHFwK', 'admin@quotationsystem.com', 'ADMIN', 1),
('superadmin', '$2a$10$75wTrGA8RTZSlkoFxIfS8e.UbqmUlezIPafiJiK9tuUWpvNBwHFwK', 'superadmin@quotationsystem.com', 'SUPERADMIN', 1),
('user1', '$2a$10$75wTrGA8RTZSlkoFxIfS8e.UbqmUlezIPafiJiK9tuUWpvNBwHFwK', 'user1@quotationsystem.com', 'USER', 1),
('user2', '$2a$10$75wTrGA8RTZSlkoFxIfS8e.UbqmUlezIPafiJiK9tuUWpvNBwHFwK', 'user2@quotationsystem.com', 'USER', 1);

-- Insert Companies
INSERT INTO companies (name, address, phone, email) VALUES
('Acme Corporation', '123 Business St, New York, NY 10001', '+1-555-0101', 'contact@acmecorp.com'),
('Global Tech Solutions', '456 Innovation Ave, San Francisco, CA 94102', '+1-555-0102', 'info@globaltech.com'),
('Premier Industries', '789 Industrial Blvd, Chicago, IL 60601', '+1-555-0103', 'sales@premierindustries.com'),
('Sunrise Enterprises', '321 Commerce Dr, Austin, TX 78701', '+1-555-0104', 'hello@sunriseent.com'),
('Metro Services LLC', '654 Market St, Boston, MA 02101', '+1-555-0105', 'contact@metroservices.com'),
('Pacific Trading Co', '987 Harbor Way, Seattle, WA 98101', '+1-555-0106', 'info@pacifictrading.com'),
('Eastern Manufacturing', '147 Factory Rd, Philadelphia, PA 19101', '+1-555-0107', 'sales@easternmfg.com'),
('Western Distributors', '258 Warehouse Ln, Denver, CO 80201', '+1-555-0108', 'orders@westerndist.com'),
('Northern Solutions', '369 Office Park, Minneapolis, MN 55401', '+1-555-0109', 'contact@northernsol.com'),
('Southern Supplies Inc', '741 Trade Center, Atlanta, GA 30301', '+1-555-0110', 'info@southernsupplies.com');

-- Insert Products
INSERT INTO products (name, description, base_price, category) VALUES
-- Office Supplies
('Premium Laptop', 'High-performance business laptop with 16GB RAM', 1299.99, 'Hardware'),
('Wireless Mouse', 'Ergonomic wireless mouse with USB receiver', 29.99, 'Hardware'),
('Mechanical Keyboard', 'Professional mechanical keyboard with RGB lighting', 149.99, 'Hardware'),
('27" Monitor', '4K UHD monitor with HDR support', 449.99, 'Hardware'),
('Office Chair', 'Ergonomic office chair with lumbar support', 299.99, 'Furniture'),
('Standing Desk', 'Electric height-adjustable standing desk', 599.99, 'Furniture'),
('Desk Lamp', 'LED desk lamp with adjustable brightness', 49.99, 'Office Supplies'),
('Webcam HD', '1080p HD webcam with built-in microphone', 79.99, 'Hardware'),
('Headset', 'Noise-cancelling wireless headset', 199.99, 'Hardware'),
('USB Hub', '7-port USB 3.0 hub with power adapter', 39.99, 'Hardware'),

-- Software & Services
('Software License', 'Annual software license for productivity suite', 499.99, 'Software'),
('Cloud Storage', 'Enterprise cloud storage - 1TB per user/year', 120.00, 'Software'),
('Security Suite', 'Comprehensive security software package', 89.99, 'Software'),
('Project Management Tool', 'Annual subscription for project management platform', 299.99, 'Software'),
('Video Conferencing', 'Professional video conferencing solution', 199.99, 'Software'),

-- Hardware & Equipment
('Network Router', 'Enterprise-grade wireless router', 249.99, 'Hardware'),
('External SSD', '1TB external solid state drive', 129.99, 'Hardware'),
('Printer', 'All-in-one color laser printer', 399.99, 'Hardware'),
('Scanner', 'High-speed document scanner', 299.99, 'Hardware'),
('Projector', 'Full HD business projector', 699.99, 'Hardware'),

-- Furniture & Accessories
('Filing Cabinet', '4-drawer locking filing cabinet', 199.99, 'Furniture'),
('Bookshelf', '5-tier wooden bookshelf', 149.99, 'Furniture'),
('Whiteboard', '6ft x 4ft magnetic whiteboard', 179.99, 'Office Supplies'),
('Conference Table', 'Large conference table seats 10', 899.99, 'Furniture'),
('Office Plant', 'Low-maintenance office plant with pot', 49.99, 'Office Supplies'),

-- Consumables
('Printer Paper', 'Case of 10 reams (5000 sheets)', 45.99, 'Consumables'),
('Toner Cartridge', 'High-yield black toner cartridge', 89.99, 'Consumables'),
('Pens (Box)', 'Box of 50 ballpoint pens', 12.99, 'Consumables'),
('Notebooks', 'Pack of 10 spiral notebooks', 24.99, 'Consumables'),
('Sticky Notes', 'Assorted colors sticky notes pack', 9.99, 'Consumables');

-- Insert Sample Quotations with different statuses
-- Get user IDs (assuming they start from 1)
SET @user1_id = (SELECT id FROM users WHERE username = 'user1' LIMIT 1);
SET @user2_id = (SELECT id FROM users WHERE username = 'user2' LIMIT 1);
SET @admin_id = (SELECT id FROM users WHERE username = 'admin' LIMIT 1);

-- Get company IDs
SET @company1_id = (SELECT id FROM companies WHERE name = 'Acme Corporation' LIMIT 1);
SET @company2_id = (SELECT id FROM companies WHERE name = 'Global Tech Solutions' LIMIT 1);
SET @company3_id = (SELECT id FROM companies WHERE name = 'Premier Industries' LIMIT 1);
SET @company4_id = (SELECT id FROM companies WHERE name = 'Sunrise Enterprises' LIMIT 1);
SET @company5_id = (SELECT id FROM companies WHERE name = 'Metro Services LLC' LIMIT 1);

-- Quotation 1: PENDING_APPROVAL
INSERT INTO quotations (quotation_number, company_id, status, total_amount, created_by, follow_up_date)
VALUES ('QT-2026-001', @company1_id, 'PENDING_APPROVAL', 1879.96, @user1_id, DATE_ADD(CURDATE(), INTERVAL 5 DAY));

SET @quot1_id = LAST_INSERT_ID();

INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot1_id, id, 2, base_price, base_price * 2 FROM products WHERE name = 'Premium Laptop' LIMIT 1;
INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot1_id, id, 5, base_price, base_price * 5 FROM products WHERE name = 'Wireless Mouse' LIMIT 1;
INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot1_id, id, 2, base_price, base_price * 2 FROM products WHERE name = 'Mechanical Keyboard' LIMIT 1;

-- Quotation 2: APPROVED
INSERT INTO quotations (quotation_number, company_id, status, total_amount, created_by, approved_by, follow_up_date)
VALUES ('QT-2026-002', @company2_id, 'APPROVED', 2499.95, @user1_id, @admin_id, DATE_ADD(CURDATE(), INTERVAL 10 DAY));

SET @quot2_id = LAST_INSERT_ID();

INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot2_id, id, 5, base_price, base_price * 5 FROM products WHERE name = 'Software License' LIMIT 1;

-- Quotation 3: DRAFT
INSERT INTO quotations (quotation_number, company_id, status, total_amount, created_by)
VALUES ('QT-2026-003', @company3_id, 'DRAFT', 1549.94, @user2_id);

SET @quot3_id = LAST_INSERT_ID();

INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot3_id, id, 2, base_price, base_price * 2 FROM products WHERE name = 'Office Chair' LIMIT 1;
INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot3_id, id, 1, base_price, base_price * 1 FROM products WHERE name = 'Standing Desk' LIMIT 1;
INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot3_id, id, 5, base_price, base_price * 5 FROM products WHERE name = 'Desk Lamp' LIMIT 1;

-- Quotation 4: SENT
INSERT INTO quotations (quotation_number, company_id, status, total_amount, created_by, approved_by, follow_up_date)
VALUES ('QT-2026-004', @company4_id, 'SENT', 3999.96, @user1_id, @admin_id, DATE_ADD(CURDATE(), INTERVAL 3 DAY));

SET @quot4_id = LAST_INSERT_ID();

INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot4_id, id, 1, base_price, base_price * 1 FROM products WHERE name = 'Conference Table' LIMIT 1;
INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot4_id, id, 10, base_price, base_price * 10 FROM products WHERE name = 'Office Chair' LIMIT 1;

-- Quotation 5: REJECTED (with rejection reason)
INSERT INTO quotations (quotation_number, company_id, status, total_amount, created_by, approved_by, rejection_reason)
VALUES ('QT-2026-005', @company5_id, 'REJECTED', 899.97, @user2_id, @admin_id, 'Prices need to be adjusted. Please revise with 10% discount.');

SET @quot5_id = LAST_INSERT_ID();

INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot5_id, id, 3, base_price, base_price * 3 FROM products WHERE name = 'Printer' LIMIT 1;

-- Quotation 6: PENDING_APPROVAL
INSERT INTO quotations (quotation_number, company_id, status, total_amount, created_by, follow_up_date)
VALUES ('QT-2026-006', @company1_id, 'PENDING_APPROVAL', 1799.94, @user1_id, DATE_ADD(CURDATE(), INTERVAL 20 DAY));

SET @quot6_id = LAST_INSERT_ID();

INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot6_id, id, 4, base_price, base_price * 4 FROM products WHERE name = '27" Monitor' LIMIT 1;

-- Quotation 7: APPROVED
INSERT INTO quotations (quotation_number, company_id, status, total_amount, created_by, approved_by, follow_up_date)
VALUES ('QT-2026-007', @company2_id, 'APPROVED', 2399.88, @user1_id, @admin_id, DATE_ADD(CURDATE(), INTERVAL 7 DAY));

SET @quot7_id = LAST_INSERT_ID();

INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot7_id, id, 12, base_price, base_price * 12 FROM products WHERE name = 'Headset' LIMIT 1;

-- Quotation 8: DRAFT (another draft for testing)
INSERT INTO quotations (quotation_number, company_id, status, total_amount, created_by)
VALUES ('QT-2026-008', @company5_id, 'DRAFT', 809.97, @user2_id);

SET @quot8_id = LAST_INSERT_ID();

INSERT INTO quotation_items (quotation_id, product_id, quantity, unit_price, total_price)
SELECT @quot8_id, id, 3, base_price * 0.9, base_price * 0.9 * 3 FROM products WHERE name = 'Printer' LIMIT 1;

-- Display summary
SELECT 'Test data inserted successfully!' AS Status;
SELECT COUNT(*) AS 'Total Users' FROM users;
SELECT COUNT(*) AS 'Total Companies' FROM companies;
SELECT COUNT(*) AS 'Total Products' FROM products;
SELECT COUNT(*) AS 'Total Quotations' FROM quotations;
SELECT COUNT(*) AS 'Total Quotation Items' FROM quotation_items;

-- Display login credentials
SELECT '=== LOGIN CREDENTIALS ===' AS Info;
SELECT username, 'password123' AS password, role FROM users ORDER BY role DESC, username;
