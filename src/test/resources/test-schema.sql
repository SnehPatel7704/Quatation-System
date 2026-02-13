-- Test schema for QuotationRepository tests
-- This schema includes all fields including the new ones for approval workflow

DROP TABLE IF EXISTS quotation_items;
DROP TABLE IF EXISTS option_groups;
DROP TABLE IF EXISTS quotations;
DROP TABLE IF EXISTS companies;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create companies table
CREATE TABLE companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create products table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    unit_price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create index on category column
CREATE INDEX idx_products_category ON products(category);

-- Create quotations table with new fields
CREATE TABLE quotations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quotation_number VARCHAR(50) NOT NULL UNIQUE,
    company_id BIGINT NOT NULL,
    template_id BIGINT,
    status VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    created_by BIGINT NOT NULL,
    approved_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    follow_up_date DATE,
    rejection_reason TEXT,
    revision_number INT NOT NULL DEFAULT 1,
    parent_quotation_id BIGINT,
    FOREIGN KEY (company_id) REFERENCES companies(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    FOREIGN KEY (parent_quotation_id) REFERENCES quotations(id) ON DELETE SET NULL
);

-- Create indexes
CREATE INDEX idx_follow_up_date ON quotations(follow_up_date);
CREATE INDEX idx_status ON quotations(status);
CREATE INDEX idx_parent_quotation ON quotations(parent_quotation_id);

-- Create option_groups table
CREATE TABLE option_groups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quotation_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    is_selected BOOLEAN NOT NULL DEFAULT FALSE,
    subtotal DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quotation_id) REFERENCES quotations(id) ON DELETE CASCADE
);

-- Create indexes for option_groups
CREATE INDEX idx_option_groups_quotation ON option_groups(quotation_id);
CREATE INDEX idx_option_groups_selected ON option_groups(quotation_id, is_selected);

-- Create quotation_items table
CREATE TABLE quotation_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quotation_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (quotation_id) REFERENCES quotations(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Insert test data
INSERT INTO users (id, username, email, password, role) VALUES
(1, 'testuser', 'test@example.com', 'password', 'USER'),
(2, 'admin', 'admin@example.com', 'password', 'ADMIN');

INSERT INTO companies (id, name, email, phone, address) VALUES
(1, 'Test Company', 'company@example.com', '123-456-7890', '123 Test St');

INSERT INTO products (id, name, description, unit_price) VALUES
(1, 'Test Product', 'Test Description', 100.00);
