package com.quotation.util;

import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import com.quotation.model.User;
import com.quotation.util.exception.AuthorizationException;
import com.quotation.util.exception.ValidationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for shared validation logic across the application.
 * Provides methods for validating quotations, items, date ranges, and user roles.
 */
public class ValidationUtil {

    private ValidationUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates a quotation object.
     * 
     * @param quotation the quotation to validate
     * @throws ValidationException if validation fails
     */
    public static void validateQuotation(Quotation quotation) {
        if (quotation == null) {
            throw new ValidationException("Quotation cannot be null");
        }

        if (quotation.getCompanyId() == null) {
            throw new ValidationException("Company ID is required");
        }

        if (quotation.getTotalAmount() != null && quotation.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Total amount cannot be negative");
        }
    }

    /**
     * Validates a list of quotation items.
     * 
     * @param items the list of items to validate
     * @throws ValidationException if validation fails
     */
    public static void validateQuotationItems(List<QuotationItem> items) {
        if (items == null || items.isEmpty()) {
            throw new ValidationException("At least one item is required");
        }

        for (int i = 0; i < items.size(); i++) {
            QuotationItem item = items.get(i);
            
            if (item.getProductId() == null) {
                throw new ValidationException("Product ID is required for item " + (i + 1));
            }

            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new ValidationException("Quantity must be a positive integer for item " + (i + 1));
            }

            if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Unit price must be a positive number for item " + (i + 1));
            }
        }
    }

    /**
     * Validates a date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @throws ValidationException if validation fails
     */
    public static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new ValidationException("Start date is required");
        }

        if (endDate == null) {
            throw new ValidationException("End date is required");
        }

        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date must be before or equal to end date");
        }
    }

    /**
     * Validates that the authenticated user has one of the required roles.
     * 
     * @param auth the authentication object
     * @param requiredRoles the roles that are allowed
     * @throws AuthorizationException if the user doesn't have any of the required roles
     */
    public static void validateUserRole(Authentication auth, String... requiredRoles) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new AuthorizationException("User is not authenticated");
        }

        List<String> userAuthorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        boolean hasRequiredRole = Arrays.stream(requiredRoles)
                .anyMatch(userAuthorities::contains);

        if (!hasRequiredRole) {
            throw new AuthorizationException(
                "User does not have required role. Required: " + 
                Arrays.toString(requiredRoles) + ", User has: " + userAuthorities
            );
        }
    }

    /**
     * Validates that a quotation can be edited based on its status.
     * Only DRAFT quotations can be edited.
     * 
     * @param quotation the quotation to check
     * @throws ValidationException if the quotation cannot be edited
     */
    public static void validateQuotationEditable(Quotation quotation) {
        if (quotation == null) {
            throw new ValidationException("Quotation cannot be null");
        }

        if (quotation.getStatus() != Quotation.QuotationStatus.DRAFT) {
            throw new ValidationException(
                "Only quotations with DRAFT status can be edited. Current status: " + 
                quotation.getStatus()
            );
        }
    }

    /**
     * Validates that a quotation status transition is valid.
     * 
     * @param currentStatus the current status
     * @param newStatus the new status
     * @throws ValidationException if the transition is not valid
     */
    public static void validateStatusTransition(Quotation.QuotationStatus currentStatus, 
                                                Quotation.QuotationStatus newStatus) {
        if (currentStatus == null || newStatus == null) {
            throw new ValidationException("Status cannot be null");
        }

        // SENT is a terminal status
        if (currentStatus == Quotation.QuotationStatus.SENT) {
            throw new ValidationException("Cannot change status of a SENT quotation");
        }

        // Valid transitions:
        // PENDING_APPROVAL -> APPROVED
        // PENDING_APPROVAL -> REJECTED
        // APPROVED -> SENT
        boolean isValidTransition = false;

        if (currentStatus == Quotation.QuotationStatus.PENDING_APPROVAL) {
            isValidTransition = newStatus == Quotation.QuotationStatus.APPROVED || 
                               newStatus == Quotation.QuotationStatus.REJECTED;
        } else if (currentStatus == Quotation.QuotationStatus.APPROVED) {
            isValidTransition = newStatus == Quotation.QuotationStatus.SENT;
        }

        if (!isValidTransition) {
            throw new ValidationException(
                "Invalid status transition from " + currentStatus + " to " + newStatus
            );
        }
    }

    /**
     * Validates that a string is not null or blank.
     * 
     * @param value the string to validate
     * @param fieldName the name of the field (for error messages)
     * @throws ValidationException if the string is null or blank
     */
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required");
        }
    }

    /**
     * Validates that an ID is not null and positive.
     * 
     * @param id the ID to validate
     * @param fieldName the name of the field (for error messages)
     * @throws ValidationException if the ID is invalid
     */
    public static void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new ValidationException(fieldName + " must be a positive number");
        }
    }
}
