# Database Migration Scripts

This directory contains SQL migration scripts for the Quotation System database.

## Running Migrations

Since Flyway is not currently configured in this project, migrations must be run manually.

### Manual Migration

1. **Backup your database first:**
   ```bash
   mysqldump -u root -p quotation_db > backup_$(date +%Y%m%d_%H%M%S).sql
   ```

2. **Run the migration script:**
   ```bash
   mysql -u root -p quotation_db < src/main/resources/db/migration/V1__add_approval_workflow_fields.sql
   ```

3. **Verify the migration:**
   ```bash
   mysql -u root -p quotation_db -e "DESCRIBE quotations;"
   ```

   You should see the new columns:
   - `follow_up_date` (DATE, NULL)
   - `rejection_reason` (TEXT, NULL)
   - `revision_number` (INT, NOT NULL, DEFAULT 1)
   - `parent_quotation_id` (BIGINT, NULL)

### Migration Files

- **V1__add_approval_workflow_fields.sql**: Adds columns for approval workflow and follow-up tracking
  - Adds `follow_up_date` column for tracking follow-up dates
  - Adds `rejection_reason` column for storing rejection reasons
  - Adds `revision_number` column for tracking quotation versions
  - Adds `parent_quotation_id` column for linking revisions
  - Creates indexes on `follow_up_date`, `status`, and `parent_quotation_id`
  - Sets `revision_number = 1` for all existing quotations

## Future: Configuring Flyway

To automate migrations in the future, add Flyway to `pom.xml`:

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

And update `application.properties`:

```properties
# Change from create to validate
spring.jpa.hibernate.ddl-auto=validate

# Enable Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

With Flyway configured, migrations will run automatically on application startup.

## Rollback

If you need to rollback the migration:

```sql
-- Remove indexes
DROP INDEX idx_follow_up_date ON quotations;
DROP INDEX idx_status ON quotations;
DROP INDEX idx_parent_quotation ON quotations;

-- Remove foreign key constraint
ALTER TABLE quotations DROP FOREIGN KEY fk_parent_quotation;

-- Remove columns
ALTER TABLE quotations DROP COLUMN parent_quotation_id;
ALTER TABLE quotations DROP COLUMN revision_number;
ALTER TABLE quotations DROP COLUMN rejection_reason;
ALTER TABLE quotations DROP COLUMN follow_up_date;
```

## Notes

- Always backup your database before running migrations
- Test migrations on a development database first
- The migration is idempotent - it can be run multiple times safely (though it will fail if columns already exist)
- Existing quotations will have `revision_number` set to 1 automatically
