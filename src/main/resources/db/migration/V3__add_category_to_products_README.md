# Migration V3: Add Category to Products

## Overview
This migration adds a `category` column to the `products` table to support product categorization and filtering.

## Changes Made
1. Added `category VARCHAR(100)` column to `products` table (nullable)
2. Created index `idx_products_category` on the `category` column for performance

## Requirements
- **Feature**: quotation-product-enhancements
- **Requirements**: 1.1, 1.2

## Standard Categories
The system supports the following standard categories:
- Office Supplies
- Software
- Hardware
- Furniture
- Consumables

Note: The category field accepts any string value for flexibility.

## Running the Migration

### Manual Execution
```bash
# Backup database first
mysqldump -u root -p quotation_db > backup_$(date +%Y%m%d_%H%M%S).sql

# Run migration
mysql -u root -p quotation_db < src/main/resources/db/migration/V3__add_category_to_products.sql

# Verify migration
mysql -u root -p quotation_db -e "DESCRIBE products;"
mysql -u root -p quotation_db -e "SHOW INDEX FROM products WHERE Key_name = 'idx_products_category';"
```

## Verification
After running the migration, you should see:
- A `category` column in the `products` table (VARCHAR(100), nullable)
- An index named `idx_products_category` on the `category` column
- All existing products will have `category = NULL`

## Testing
The migration has been tested on the development database:
- ✅ Column added successfully
- ✅ Index created successfully
- ✅ Can insert products with categories
- ✅ Can query products by category
- ✅ Existing products remain unaffected (category = NULL)

## Rollback
If you need to rollback this migration:
```sql
-- Remove index
DROP INDEX idx_products_category ON products;

-- Remove column
ALTER TABLE products DROP COLUMN category;
```

## Related Files
- Migration script: `V3__add_category_to_products.sql`
- Test schema: `src/test/resources/test-schema.sql` (updated)
- Test data: `test-data.sql` (updated with sample categories)

## Next Steps
After this migration:
1. Update the Product model to include the category field (Task 2.1)
2. Enhance ProductRepository with category methods (Task 3.1)
3. Update the frontend to display and filter by category
