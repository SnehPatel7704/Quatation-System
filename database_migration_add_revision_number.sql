-- Migration: Add revision tracking columns to quotations table
-- Date: 2026-02-11
-- Description: Adds revision_number and parent_quotation_id columns to support quotation revisions

-- Add revision_number column (skip if exists)
SET @dbname = DATABASE();
SET @tablename = 'quotations';
SET @columnname = 'revision_number';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  'ALTER TABLE quotations ADD COLUMN revision_number INT DEFAULT 1 NOT NULL'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Add parent_quotation_id column (skip if exists)
SET @columnname = 'parent_quotation_id';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  'ALTER TABLE quotations ADD COLUMN parent_quotation_id BIGINT NULL'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Update existing records to have revision_number = 1
UPDATE quotations 
SET revision_number = 1 
WHERE revision_number IS NULL OR revision_number = 0;

-- Verify the columns were added
SELECT COLUMN_NAME, DATA_TYPE, COLUMN_DEFAULT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'quotations' 
AND COLUMN_NAME IN ('revision_number', 'parent_quotation_id');
