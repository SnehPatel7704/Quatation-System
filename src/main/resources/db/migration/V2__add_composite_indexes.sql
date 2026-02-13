-- Migration Script: Add Composite Indexes for Query Optimization
-- Feature: quotation-system-improvements
-- Requirements: 15.5
-- 
-- This migration adds composite indexes to optimize dashboard queries
-- that filter by both status and follow_up_date

-- Create composite index on (status, follow_up_date) for dashboard queries
-- This index will be used when querying upcoming follow-ups filtered by status
CREATE INDEX idx_status_follow_up_date ON quotations(status, follow_up_date);

-- Verify the indexes
SHOW INDEX FROM quotations WHERE Key_name LIKE 'idx_%';
