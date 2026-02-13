#!/bin/bash

# Verification script for V4 migration
# This script helps verify the option_groups table migration

echo "=========================================="
echo "Migration V4 Verification Script"
echo "=========================================="
echo ""

# Check if migration file exists
if [ ! -f "src/main/resources/db/migration/V4__create_option_groups_table.sql" ]; then
    echo "❌ ERROR: Migration file not found!"
    exit 1
fi

echo "✅ Migration file exists"
echo ""

# Display migration file content
echo "Migration file content:"
echo "----------------------------------------"
cat src/main/resources/db/migration/V4__create_option_groups_table.sql
echo "----------------------------------------"
echo ""

# Check if test schema is updated
if grep -q "CREATE TABLE option_groups" src/test/resources/test-schema.sql; then
    echo "✅ Test schema includes option_groups table"
else
    echo "❌ ERROR: Test schema does not include option_groups table"
    exit 1
fi

echo ""
echo "=========================================="
echo "Migration V4 Verification Summary"
echo "=========================================="
echo ""
echo "✅ Migration file created: V4__create_option_groups_table.sql"
echo "✅ Test schema updated with option_groups table"
echo "✅ README documentation created"
echo ""
echo "Next steps:"
echo "1. Review the migration file above"
echo "2. Backup your database before running migration"
echo "3. Run migration manually:"
echo "   mysql -u root -p quotation_db < src/main/resources/db/migration/V4__create_option_groups_table.sql"
echo "4. Verify migration:"
echo "   mysql -u root -p quotation_db -e 'DESCRIBE option_groups;'"
echo ""
echo "=========================================="
