package com.quotation.util;

import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import com.quotation.util.exception.AuthorizationException;
import com.quotation.util.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationUtil.
 * Tests validation methods with valid and invalid inputs, and edge cases.
 */
class ValidationUtilTest {

    // ========== validateQuotation Tests ==========

    @Test
    void validateQuotation_withValidQuotation_shouldNotThrow() {
        Quotation quotation = new Quotation();
        quotation.setCompanyId(1L);
        quotation.setTotalAmount(BigDecimal.valueOf(100.00));

        assertDoesNotThrow(() -> ValidationUtil.validateQuotation(quotation));
    }

    @Test
    void validateQuotation_withNullQuotation_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotation(null)
        );
        assertEquals("Quotation cannot be null", exception.getMessage());
    }

    @Test
    void validateQuotation_withNullCompanyId_shouldThrowValidationException() {
        Quotation quotation = new Quotation();
        quotation.setCompanyId(null);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotation(quotation)
        );
        assertEquals("Company ID is required", exception.getMessage());
    }

    @Test
    void validateQuotation_withNegativeTotalAmount_shouldThrowValidationException() {
        Quotation quotation = new Quotation();
        quotation.setCompanyId(1L);
        quotation.setTotalAmount(BigDecimal.valueOf(-100.00));

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotation(quotation)
        );
        assertEquals("Total amount cannot be negative", exception.getMessage());
    }

    @Test
    void validateQuotation_withZeroTotalAmount_shouldNotThrow() {
        Quotation quotation = new Quotation();
        quotation.setCompanyId(1L);
        quotation.setTotalAmount(BigDecimal.ZERO);

        assertDoesNotThrow(() -> ValidationUtil.validateQuotation(quotation));
    }

    @Test
    void validateQuotation_withNullTotalAmount_shouldNotThrow() {
        Quotation quotation = new Quotation();
        quotation.setCompanyId(1L);
        quotation.setTotalAmount(null);

        assertDoesNotThrow(() -> ValidationUtil.validateQuotation(quotation));
    }

    // ========== validateQuotationItems Tests ==========

    @Test
    void validateQuotationItems_withValidItems_shouldNotThrow() {
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(5);
        item.setUnitPrice(BigDecimal.valueOf(10.00));
        items.add(item);

        assertDoesNotThrow(() -> ValidationUtil.validateQuotationItems(items));
    }

    @Test
    void validateQuotationItems_withNullList_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(null)
        );
        assertEquals("At least one item is required", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withEmptyList_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(Collections.emptyList())
        );
        assertEquals("At least one item is required", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withNullProductId_shouldThrowValidationException() {
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(null);
        item.setQuantity(5);
        item.setUnitPrice(BigDecimal.valueOf(10.00));
        items.add(item);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(items)
        );
        assertEquals("Product ID is required for item 1", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withNullQuantity_shouldThrowValidationException() {
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(null);
        item.setUnitPrice(BigDecimal.valueOf(10.00));
        items.add(item);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(items)
        );
        assertEquals("Quantity must be a positive integer for item 1", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withZeroQuantity_shouldThrowValidationException() {
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(0);
        item.setUnitPrice(BigDecimal.valueOf(10.00));
        items.add(item);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(items)
        );
        assertEquals("Quantity must be a positive integer for item 1", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withNegativeQuantity_shouldThrowValidationException() {
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(-5);
        item.setUnitPrice(BigDecimal.valueOf(10.00));
        items.add(item);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(items)
        );
        assertEquals("Quantity must be a positive integer for item 1", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withNullUnitPrice_shouldThrowValidationException() {
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(5);
        item.setUnitPrice(null);
        items.add(item);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(items)
        );
        assertEquals("Unit price must be a positive number for item 1", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withZeroUnitPrice_shouldThrowValidationException() {
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(5);
        item.setUnitPrice(BigDecimal.ZERO);
        items.add(item);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(items)
        );
        assertEquals("Unit price must be a positive number for item 1", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withNegativeUnitPrice_shouldThrowValidationException() {
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(5);
        item.setUnitPrice(BigDecimal.valueOf(-10.00));
        items.add(item);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(items)
        );
        assertEquals("Unit price must be a positive number for item 1", exception.getMessage());
    }

    @Test
    void validateQuotationItems_withMultipleItems_shouldValidateAll() {
        List<QuotationItem> items = new ArrayList<>();
        
        QuotationItem item1 = new QuotationItem();
        item1.setProductId(1L);
        item1.setQuantity(5);
        item1.setUnitPrice(BigDecimal.valueOf(10.00));
        items.add(item1);
        
        QuotationItem item2 = new QuotationItem();
        item2.setProductId(2L);
        item2.setQuantity(3);
        item2.setUnitPrice(BigDecimal.valueOf(20.00));
        items.add(item2);

        assertDoesNotThrow(() -> ValidationUtil.validateQuotationItems(items));
    }

    @Test
    void validateQuotationItems_withInvalidSecondItem_shouldThrowWithCorrectItemNumber() {
        List<QuotationItem> items = new ArrayList<>();
        
        QuotationItem item1 = new QuotationItem();
        item1.setProductId(1L);
        item1.setQuantity(5);
        item1.setUnitPrice(BigDecimal.valueOf(10.00));
        items.add(item1);
        
        QuotationItem item2 = new QuotationItem();
        item2.setProductId(2L);
        item2.setQuantity(-3);  // Invalid
        item2.setUnitPrice(BigDecimal.valueOf(20.00));
        items.add(item2);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationItems(items)
        );
        assertEquals("Quantity must be a positive integer for item 2", exception.getMessage());
    }

    // ========== validateDateRange Tests ==========

    @Test
    void validateDateRange_withValidRange_shouldNotThrow() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        assertDoesNotThrow(() -> ValidationUtil.validateDateRange(startDate, endDate));
    }

    @Test
    void validateDateRange_withSameDate_shouldNotThrow() {
        LocalDate date = LocalDate.of(2024, 1, 1);

        assertDoesNotThrow(() -> ValidationUtil.validateDateRange(date, date));
    }

    @Test
    void validateDateRange_withNullStartDate_shouldThrowValidationException() {
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateDateRange(null, endDate)
        );
        assertEquals("Start date is required", exception.getMessage());
    }

    @Test
    void validateDateRange_withNullEndDate_shouldThrowValidationException() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateDateRange(startDate, null)
        );
        assertEquals("End date is required", exception.getMessage());
    }

    @Test
    void validateDateRange_withStartAfterEnd_shouldThrowValidationException() {
        LocalDate startDate = LocalDate.of(2024, 12, 31);
        LocalDate endDate = LocalDate.of(2024, 1, 1);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateDateRange(startDate, endDate)
        );
        assertEquals("Start date must be before or equal to end date", exception.getMessage());
    }

    // ========== validateUserRole Tests ==========

    @Test
    void validateUserRole_withValidRole_shouldNotThrow() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "user", "password", 
            Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
        );

        assertDoesNotThrow(() -> ValidationUtil.validateUserRole(auth, "ADMIN"));
    }

    @Test
    void validateUserRole_withMultipleValidRoles_shouldNotThrow() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "user", "password", 
            Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
        );

        assertDoesNotThrow(() -> ValidationUtil.validateUserRole(auth, "ADMIN", "SUPERADMIN"));
    }

    @Test
    void validateUserRole_withNullAuthentication_shouldThrowAuthorizationException() {
        AuthorizationException exception = assertThrows(
            AuthorizationException.class,
            () -> ValidationUtil.validateUserRole(null, "ADMIN")
        );
        assertEquals("User is not authenticated", exception.getMessage());
    }

    @Test
    void validateUserRole_withInvalidRole_shouldThrowAuthorizationException() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "user", "password", 
            Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );

        AuthorizationException exception = assertThrows(
            AuthorizationException.class,
            () -> ValidationUtil.validateUserRole(auth, "ADMIN")
        );
        assertTrue(exception.getMessage().contains("User does not have required role"));
    }

    // ========== validateQuotationEditable Tests ==========

    @Test
    void validateQuotationEditable_withDraftStatus_shouldNotThrow() {
        Quotation quotation = new Quotation();
        quotation.setStatus(Quotation.QuotationStatus.DRAFT);

        assertDoesNotThrow(() -> ValidationUtil.validateQuotationEditable(quotation));
    }

    @Test
    void validateQuotationEditable_withNullQuotation_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationEditable(null)
        );
        assertEquals("Quotation cannot be null", exception.getMessage());
    }

    @Test
    void validateQuotationEditable_withPendingApprovalStatus_shouldThrowValidationException() {
        Quotation quotation = new Quotation();
        quotation.setStatus(Quotation.QuotationStatus.PENDING_APPROVAL);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationEditable(quotation)
        );
        assertTrue(exception.getMessage().contains("Only quotations with DRAFT status can be edited"));
    }

    @Test
    void validateQuotationEditable_withApprovedStatus_shouldThrowValidationException() {
        Quotation quotation = new Quotation();
        quotation.setStatus(Quotation.QuotationStatus.APPROVED);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateQuotationEditable(quotation)
        );
        assertTrue(exception.getMessage().contains("Only quotations with DRAFT status can be edited"));
    }

    // ========== validateStatusTransition Tests ==========

    @Test
    void validateStatusTransition_fromPendingToApproved_shouldNotThrow() {
        assertDoesNotThrow(() -> ValidationUtil.validateStatusTransition(
            Quotation.QuotationStatus.PENDING_APPROVAL,
            Quotation.QuotationStatus.APPROVED
        ));
    }

    @Test
    void validateStatusTransition_fromPendingToRejected_shouldNotThrow() {
        assertDoesNotThrow(() -> ValidationUtil.validateStatusTransition(
            Quotation.QuotationStatus.PENDING_APPROVAL,
            Quotation.QuotationStatus.REJECTED
        ));
    }

    @Test
    void validateStatusTransition_fromApprovedToSent_shouldNotThrow() {
        assertDoesNotThrow(() -> ValidationUtil.validateStatusTransition(
            Quotation.QuotationStatus.APPROVED,
            Quotation.QuotationStatus.SENT
        ));
    }

    @Test
    void validateStatusTransition_fromSentToAny_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateStatusTransition(
                Quotation.QuotationStatus.SENT,
                Quotation.QuotationStatus.APPROVED
            )
        );
        assertEquals("Cannot change status of a SENT quotation", exception.getMessage());
    }

    @Test
    void validateStatusTransition_withNullCurrentStatus_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateStatusTransition(null, Quotation.QuotationStatus.APPROVED)
        );
        assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    void validateStatusTransition_withNullNewStatus_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateStatusTransition(Quotation.QuotationStatus.PENDING_APPROVAL, null)
        );
        assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    void validateStatusTransition_invalidTransition_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateStatusTransition(
                Quotation.QuotationStatus.DRAFT,
                Quotation.QuotationStatus.APPROVED
            )
        );
        assertTrue(exception.getMessage().contains("Invalid status transition"));
    }

    // ========== validateNotBlank Tests ==========

    @Test
    void validateNotBlank_withValidString_shouldNotThrow() {
        assertDoesNotThrow(() -> ValidationUtil.validateNotBlank("test", "Field"));
    }

    @Test
    void validateNotBlank_withNullString_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateNotBlank(null, "Field")
        );
        assertEquals("Field is required", exception.getMessage());
    }

    @Test
    void validateNotBlank_withEmptyString_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateNotBlank("", "Field")
        );
        assertEquals("Field is required", exception.getMessage());
    }

    @Test
    void validateNotBlank_withWhitespaceString_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateNotBlank("   ", "Field")
        );
        assertEquals("Field is required", exception.getMessage());
    }

    // ========== validateId Tests ==========

    @Test
    void validateId_withValidId_shouldNotThrow() {
        assertDoesNotThrow(() -> ValidationUtil.validateId(1L, "ID"));
    }

    @Test
    void validateId_withNullId_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateId(null, "ID")
        );
        assertEquals("ID must be a positive number", exception.getMessage());
    }

    @Test
    void validateId_withZeroId_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateId(0L, "ID")
        );
        assertEquals("ID must be a positive number", exception.getMessage());
    }

    @Test
    void validateId_withNegativeId_shouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationUtil.validateId(-1L, "ID")
        );
        assertEquals("ID must be a positive number", exception.getMessage());
    }
}
