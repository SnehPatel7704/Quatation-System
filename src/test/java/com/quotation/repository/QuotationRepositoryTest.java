package com.quotation.repository;

import com.quotation.model.Quotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for QuotationRepository
 * Tests new methods added for approval workflow and follow-up tracking
 * Requirements: 8.3, 8.7, 8.11
 */
@SpringBootTest
@Transactional
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class QuotationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QuotationRepository quotationRepository;

    private Quotation createTestQuotation(String quotationNumber, LocalDate followUpDate, 
                                          Quotation.QuotationStatus status, 
                                          Long parentQuotationId, Integer revisionNumber) {
        Quotation quotation = new Quotation();
        quotation.setQuotationNumber(quotationNumber);
        quotation.setCompanyId(1L);
        quotation.setStatus(status);
        quotation.setTotalAmount(new BigDecimal("1000.00"));
        quotation.setCreatedBy(1L);
        quotation.setFollowUpDate(followUpDate);
        quotation.setParentQuotationId(parentQuotationId);
        quotation.setRevisionNumber(revisionNumber);
        return quotation;
    }

    @Test
    void testFindByFollowUpDateBetween_WithQuotationsInRange() {
        // Given: Quotations with various follow-up dates
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusDays(7);
        LocalDate nextMonth = today.plusDays(30);

        Quotation q1 = createTestQuotation("Q001", tomorrow, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        Quotation q2 = createTestQuotation("Q002", nextWeek, Quotation.QuotationStatus.APPROVED, null, 1);
        Quotation q3 = createTestQuotation("Q003", nextMonth, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        Quotation q4 = createTestQuotation("Q004", null, Quotation.QuotationStatus.DRAFT, null, 1); // No follow-up date

        quotationRepository.save(q1);
        quotationRepository.save(q2);
        quotationRepository.save(q3);
        quotationRepository.save(q4);

        // When: Query for quotations in next 15 days
        LocalDate startDate = today;
        LocalDate endDate = today.plusDays(15);
        List<Quotation> results = quotationRepository.findByFollowUpDateBetween(startDate, endDate);

        // Then: Should return only quotations within the date range
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Quotation::getQuotationNumber)
                          .containsExactly("Q001", "Q002"); // Ordered by follow_up_date ASC
    }

    @Test
    void testFindByFollowUpDateBetween_WithNoQuotationsInRange() {
        // Given: Quotations with follow-up dates outside the range
        LocalDate today = LocalDate.now();
        LocalDate farFuture = today.plusDays(100);

        Quotation q1 = createTestQuotation("Q001", farFuture, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        quotationRepository.save(q1);

        // When: Query for quotations in next 15 days
        LocalDate startDate = today;
        LocalDate endDate = today.plusDays(15);
        List<Quotation> results = quotationRepository.findByFollowUpDateBetween(startDate, endDate);

        // Then: Should return empty list
        assertThat(results).isEmpty();
    }

    @Test
    void testFindByFollowUpDateBetween_ExcludesNullFollowUpDates() {
        // Given: Quotations with and without follow-up dates
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        Quotation q1 = createTestQuotation("Q001", tomorrow, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        Quotation q2 = createTestQuotation("Q002", null, Quotation.QuotationStatus.DRAFT, null, 1);
        
        quotationRepository.save(q1);
        quotationRepository.save(q2);

        // When: Query for quotations in next 15 days
        LocalDate startDate = today;
        LocalDate endDate = today.plusDays(15);
        List<Quotation> results = quotationRepository.findByFollowUpDateBetween(startDate, endDate);

        // Then: Should only return quotations with non-null follow-up dates
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getQuotationNumber()).isEqualTo("Q001");
    }

    @Test
    void testFindByFollowUpDateBetween_OrdersByFollowUpDateAscending() {
        // Given: Quotations with different follow-up dates
        LocalDate today = LocalDate.now();
        LocalDate date1 = today.plusDays(10);
        LocalDate date2 = today.plusDays(5);
        LocalDate date3 = today.plusDays(1);

        Quotation q1 = createTestQuotation("Q001", date1, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        Quotation q2 = createTestQuotation("Q002", date2, Quotation.QuotationStatus.APPROVED, null, 1);
        Quotation q3 = createTestQuotation("Q003", date3, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);

        quotationRepository.save(q1);
        quotationRepository.save(q2);
        quotationRepository.save(q3);

        // When: Query for quotations in next 15 days
        LocalDate startDate = today;
        LocalDate endDate = today.plusDays(15);
        List<Quotation> results = quotationRepository.findByFollowUpDateBetween(startDate, endDate);

        // Then: Should be ordered by follow_up_date ascending (earliest first)
        assertThat(results).hasSize(3);
        assertThat(results).extracting(Quotation::getQuotationNumber)
                          .containsExactly("Q003", "Q002", "Q001");
    }

    @Test
    void testFindByParentQuotationId_WithRevisions() {
        // Given: A parent quotation with multiple revisions
        Quotation parent = createTestQuotation("Q001", null, Quotation.QuotationStatus.REJECTED, null, 1);
        Long parentId = quotationRepository.save(parent);

        Quotation revision1 = createTestQuotation("Q001-R1", null, Quotation.QuotationStatus.DRAFT, parentId, 2);
        Quotation revision2 = createTestQuotation("Q001-R2", null, Quotation.QuotationStatus.PENDING_APPROVAL, parentId, 3);
        
        quotationRepository.save(revision1);
        quotationRepository.save(revision2);

        // When: Query for revisions of the parent quotation
        List<Quotation> revisions = quotationRepository.findByParentQuotationId(parentId);

        // Then: Should return all revisions ordered by revision number
        assertThat(revisions).hasSize(2);
        assertThat(revisions).extracting(Quotation::getQuotationNumber)
                            .containsExactly("Q001-R1", "Q001-R2");
        assertThat(revisions).extracting(Quotation::getRevisionNumber)
                            .containsExactly(2, 3);
    }

    @Test
    void testFindByParentQuotationId_WithNoRevisions() {
        // Given: A quotation with no revisions
        Quotation quotation = createTestQuotation("Q001", null, Quotation.QuotationStatus.APPROVED, null, 1);
        Long quotationId = quotationRepository.save(quotation);

        // When: Query for revisions
        List<Quotation> revisions = quotationRepository.findByParentQuotationId(quotationId);

        // Then: Should return empty list
        assertThat(revisions).isEmpty();
    }

    @Test
    void testFindByStatus_WithMultipleQuotations() {
        // Given: Quotations with various statuses
        Quotation q1 = createTestQuotation("Q001", null, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        Quotation q2 = createTestQuotation("Q002", null, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        Quotation q3 = createTestQuotation("Q003", null, Quotation.QuotationStatus.APPROVED, null, 1);
        Quotation q4 = createTestQuotation("Q004", null, Quotation.QuotationStatus.DRAFT, null, 1);

        quotationRepository.save(q1);
        quotationRepository.save(q2);
        quotationRepository.save(q3);
        quotationRepository.save(q4);

        // When: Query for PENDING_APPROVAL quotations
        List<Quotation> results = quotationRepository.findByStatus(Quotation.QuotationStatus.PENDING_APPROVAL);

        // Then: Should return only PENDING_APPROVAL quotations
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Quotation::getStatus)
                          .containsOnly(Quotation.QuotationStatus.PENDING_APPROVAL);
    }

    @Test
    void testFindByStatus_WithNoMatchingQuotations() {
        // Given: Quotations with various statuses (none REJECTED)
        Quotation q1 = createTestQuotation("Q001", null, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        Quotation q2 = createTestQuotation("Q002", null, Quotation.QuotationStatus.APPROVED, null, 1);

        quotationRepository.save(q1);
        quotationRepository.save(q2);

        // When: Query for REJECTED quotations
        List<Quotation> results = quotationRepository.findByStatus(Quotation.QuotationStatus.REJECTED);

        // Then: Should return empty list
        assertThat(results).isEmpty();
    }

    @Test
    void testSave_WithNewFields() {
        // Given: A quotation with all new fields populated
        LocalDate followUpDate = LocalDate.now().plusDays(7);
        Quotation quotation = new Quotation();
        quotation.setQuotationNumber("Q001");
        quotation.setCompanyId(1L);
        quotation.setStatus(Quotation.QuotationStatus.DRAFT);
        quotation.setTotalAmount(new BigDecimal("1500.00"));
        quotation.setCreatedBy(1L);
        quotation.setFollowUpDate(followUpDate);
        quotation.setRejectionReason("Test rejection reason");
        quotation.setRevisionNumber(2);
        quotation.setParentQuotationId(100L);

        // When: Save the quotation
        Long id = quotationRepository.save(quotation);

        // Then: Should save all fields correctly
        Optional<Quotation> savedQuotation = quotationRepository.findById(id);
        assertThat(savedQuotation).isPresent();
        assertThat(savedQuotation.get().getFollowUpDate()).isEqualTo(followUpDate);
        assertThat(savedQuotation.get().getRejectionReason()).isEqualTo("Test rejection reason");
        assertThat(savedQuotation.get().getRevisionNumber()).isEqualTo(2);
        assertThat(savedQuotation.get().getParentQuotationId()).isEqualTo(100L);
    }

    @Test
    void testSave_WithNullFollowUpDate() {
        // Given: A quotation with null follow-up date
        Quotation quotation = createTestQuotation("Q001", null, Quotation.QuotationStatus.DRAFT, null, 1);

        // When: Save the quotation
        Long id = quotationRepository.save(quotation);

        // Then: Should save with null follow-up date
        Optional<Quotation> savedQuotation = quotationRepository.findById(id);
        assertThat(savedQuotation).isPresent();
        assertThat(savedQuotation.get().getFollowUpDate()).isNull();
    }

    @Test
    void testUpdate_WithNewFields() {
        // Given: An existing quotation
        Quotation quotation = createTestQuotation("Q001", null, Quotation.QuotationStatus.DRAFT, null, 1);
        Long id = quotationRepository.save(quotation);

        // When: Update with new field values
        quotation.setId(id);
        quotation.setFollowUpDate(LocalDate.now().plusDays(10));
        quotation.setRejectionReason("Updated rejection reason");
        quotation.setRevisionNumber(2);
        quotation.setStatus(Quotation.QuotationStatus.REJECTED);
        quotationRepository.save(quotation);

        // Then: Should update all fields correctly
        Optional<Quotation> updatedQuotation = quotationRepository.findById(id);
        assertThat(updatedQuotation).isPresent();
        assertThat(updatedQuotation.get().getFollowUpDate()).isNotNull();
        assertThat(updatedQuotation.get().getRejectionReason()).isEqualTo("Updated rejection reason");
        assertThat(updatedQuotation.get().getRevisionNumber()).isEqualTo(2);
        assertThat(updatedQuotation.get().getStatus()).isEqualTo(Quotation.QuotationStatus.REJECTED);
    }

    @Test
    void testFindByFollowUpDateBetween_WithBoundaryDates() {
        // Given: Quotations with follow-up dates on boundaries
        LocalDate today = LocalDate.now();
        LocalDate startDate = today;
        LocalDate endDate = today.plusDays(10);

        Quotation q1 = createTestQuotation("Q001", startDate, Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);
        Quotation q2 = createTestQuotation("Q002", endDate, Quotation.QuotationStatus.APPROVED, null, 1);
        Quotation q3 = createTestQuotation("Q003", endDate.plusDays(1), Quotation.QuotationStatus.PENDING_APPROVAL, null, 1);

        quotationRepository.save(q1);
        quotationRepository.save(q2);
        quotationRepository.save(q3);

        // When: Query for quotations in date range
        List<Quotation> results = quotationRepository.findByFollowUpDateBetween(startDate, endDate);

        // Then: Should include boundary dates but not dates outside range
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Quotation::getQuotationNumber)
                          .containsExactly("Q001", "Q002");
    }
}
