-- Migration Script: Add Approval Workflow and Follow-up Tracking Fields
-- Feature: quotation-system-improvements
-- Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8
-- 
-- This migration adds new columns to support:
-- 1. Follow-up date tracking
-- 2. Rejection reason storage
-- 3. Quotation revision tracking
-- 4. Parent-child quotation relationships

-- Add follow_up_date column for tracking when to follow up with clients
ALTER TABLE quotations 
ADD COLUMN follow_up_date DATE NULL
COMMENT 'Date when the quotation requires follow-up action';

-- Add rejection_reason column for storing why a quotation was rejected
ALTER TABLE quotations 
ADD COLUMN rejection_reason TEXT NULL
COMMENT 'Reason provided when an admin rejects a quotation';

-- Add revision_number column for tracking quotation versions
ALTER TABLE quotations 
ADD COLUMN revision_number INT NOT NULL DEFAULT 1
COMMENT 'Version number of the quotation, incremented for each revision';

-- Add parent_quotation_id column for linking revisions to original quotations
ALTER TABLE quotations 
ADD COLUMN parent_quotation_id BIGINT NULL
COMMENT 'ID of the parent quotation if this is a revision';

-- Add foreign key constraint to ensure referential integrity
ALTER TABLE quotations 
ADD CONSTRAINT fk_parent_quotation 
FOREIGN KEY (parent_quotation_id) 
REFERENCES quotations(id) 
ON DELETE SET NULL;

-- Create index on follow_up_date for efficient date range queries
CREATE INDEX idx_follow_up_date ON quotations(follow_up_date);

-- Create index on status for efficient status filtering
CREATE INDEX idx_status ON quotations(status);

-- Create index on parent_quotation_id for efficient revision lookups
CREATE INDEX idx_parent_quotation ON quotations(parent_quotation_id);

-- Set revision_number to 1 for all existing quotations
UPDATE quotations 
SET revision_number = 1 
WHERE revision_number IS NULL OR revision_number = 0;

-- Verify the migration
-- Expected: All quotations should have revision_number = 1
-- Expected: New columns should exist and be nullable (except revision_number)
SELECT 
    COUNT(*) as total_quotations,
    COUNT(CASE WHEN revision_number = 1 THEN 1 END) as quotations_with_revision_1,
    COUNT(follow_up_date) as quotations_with_follow_up,
    COUNT(rejection_reason) as quotations_with_rejection_reason,
    COUNT(parent_quotation_id) as quotations_with_parent
FROM quotations;
