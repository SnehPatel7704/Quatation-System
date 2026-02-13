package com.quotation.controller;

import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import com.quotation.repository.QuotationRepository;
import com.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for upcoming follow-ups endpoint in QuotationController.
 * Tests the GET /api/quotations/upcoming-followups endpoint with various scenarios.
 * 
 * Requirements tested: 8.3, 8.7, 8.9, 8.11, 19.4
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.sql.init.mode=always",
    "spring.sql.init.schema-locations=classpath:test-schema.sql",
    "jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci1pbnRlZ3JhdGlvbi10ZXN0cy1taW5pbXVtLTI1Ni1iaXRzLWxvbmc="
})
@Transactional
public class QuotationControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private QuotationRepository quotationRepository;

    @BeforeEach
    public void setUp() {
        // Set up MockMvc with Spring Security
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        
        // Note: Using @Transactional annotation ensures database is rolled back after each test
    }

    /**
     * Test Case 1: Test upcoming follow-ups with 15-day date range
     * Validates: Requirements 8.3, 8.7, 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_With15DayRange_ShouldReturnQuotations() throws Exception {
        // Given: A date range of 15 days from today
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(15);
        
        // Create and save test quotations
        createAndSaveQuotations(3, startDate, endDate);

        // When & Then: Request upcoming follow-ups
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].followUpDate").exists())
                .andExpect(jsonPath("$[1].followUpDate").exists())
                .andExpect(jsonPath("$[2].followUpDate").exists());
    }

    /**
     * Test Case 2: Test upcoming follow-ups with 30-day date range
     * Validates: Requirements 8.3, 8.7, 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_With30DayRange_ShouldReturnQuotations() throws Exception {
        // Given: A date range of 30 days from today
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        
        // Create and save test quotations
        createAndSaveQuotations(5, startDate, endDate);

        // When & Then: Request upcoming follow-ups
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    /**
     * Test Case 3: Test upcoming follow-ups with 60-day (2 months) date range
     * Validates: Requirements 8.3, 8.7, 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_With60DayRange_ShouldReturnQuotations() throws Exception {
        // Given: A date range of 60 days from today
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(60);
        
        // Create and save test quotations
        createAndSaveQuotations(8, startDate, endDate);

        // When & Then: Request upcoming follow-ups
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)));
    }

    /**
     * Test Case 4: Test upcoming follow-ups with custom date range
     * Validates: Requirements 8.3, 8.7, 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_WithCustomRange_ShouldReturnQuotations() throws Exception {
        // Given: A custom date range
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = startDate.plusDays(45);
        
        // Create and save test quotations
        createAndSaveQuotations(4, startDate, endDate);

        // When & Then: Request upcoming follow-ups
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    /**
     * Test Case 5: Test upcoming follow-ups with no results
     * Validates: Requirements 8.3, 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_WithNoResults_ShouldReturnEmptyList() throws Exception {
        // Given: A date range with no quotations (far in the future)
        LocalDate startDate = LocalDate.now().plusDays(100);
        LocalDate endDate = startDate.plusDays(15);

        // When & Then: Request upcoming follow-ups
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$", empty()));
    }

    /**
     * Test Case 6: Test upcoming follow-ups sorting order (by follow_up_date ascending)
     * Validates: Requirements 8.9
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_ShouldBeSortedByDateAscending() throws Exception {
        // Given: Quotations with follow-up dates in specific order
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        
        // Create quotations with specific dates (not in order)
        createAndSaveQuotation("Q-2024-003", startDate.plusDays(20));
        createAndSaveQuotation("Q-2024-001", startDate.plusDays(5));
        createAndSaveQuotation("Q-2024-004", startDate.plusDays(25));
        createAndSaveQuotation("Q-2024-002", startDate.plusDays(10));

        // When & Then: Request upcoming follow-ups and verify sorting
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].followUpDate").value(startDate.plusDays(5).toString()))
                .andExpect(jsonPath("$[1].followUpDate").value(startDate.plusDays(10).toString()))
                .andExpect(jsonPath("$[2].followUpDate").value(startDate.plusDays(20).toString()))
                .andExpect(jsonPath("$[3].followUpDate").value(startDate.plusDays(25).toString()));
    }

    /**
     * Test Case 7: Test authorization - USER role should be denied
     * Validates: Requirements 19.4
     */
    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    public void testUpcomingFollowups_WithUserRole_ShouldBeDenied() throws Exception {
        // Given: A date range
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(15);

        // When & Then: USER role should be denied access
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    /**
     * Test Case 8: Test authorization - ADMIN role should be allowed
     * Validates: Requirements 19.4
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_WithAdminRole_ShouldBeAllowed() throws Exception {
        // Given: A date range
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(15);

        // When & Then: ADMIN role should be allowed access
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test Case 9: Test authorization - SUPERADMIN role should be allowed
     * Validates: Requirements 19.4
     */
    @Test
    @WithMockUser(username = "superadmin", authorities = {"SUPERADMIN"})
    public void testUpcomingFollowups_WithSuperAdminRole_ShouldBeAllowed() throws Exception {
        // Given: A date range
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(15);

        // When & Then: SUPERADMIN role should be allowed access
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test Case 10: Test with past date range
     * Validates: Requirements 8.3, 8.7, 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_WithPastDateRange_ShouldReturnQuotations() throws Exception {
        // Given: A past date range
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(15);
        
        // Create and save test quotations with past dates
        createAndSaveQuotations(2, startDate, endDate);

        // When & Then: Request upcoming follow-ups with past dates
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    /**
     * Test Case 11: Test with date range spanning past and future
     * Validates: Requirements 8.3, 8.7, 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_WithMixedDateRange_ShouldReturnQuotations() throws Exception {
        // Given: A date range spanning past and future
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(20);
        
        // Create and save test quotations
        createAndSaveQuotations(6, startDate, endDate);

        // When & Then: Request upcoming follow-ups
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }

    /**
     * Test Case 12: Test that only quotations within range are returned
     * Validates: Requirements 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_ShouldOnlyReturnQuotationsWithinRange() throws Exception {
        // Given: A specific date range
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(20);
        
        // Create quotations within the range
        createAndSaveQuotation("Q-IN-1", startDate.plusDays(2));
        createAndSaveQuotation("Q-IN-2", startDate.plusDays(5));
        createAndSaveQuotation("Q-IN-3", startDate.plusDays(8));
        
        // Create quotations outside the range (should not be returned)
        createAndSaveQuotation("Q-OUT-1", startDate.minusDays(5));
        createAndSaveQuotation("Q-OUT-2", endDate.plusDays(5));

        // When & Then: Verify only quotations within range are returned
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].followUpDate", everyItem(
                    allOf(
                        greaterThanOrEqualTo(startDate.toString()),
                        lessThanOrEqualTo(endDate.toString())
                    )
                )));
    }

    /**
     * Test Case 13: Test with single-day date range
     * Validates: Requirements 8.3, 8.7, 8.11
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_WithSingleDayRange_ShouldReturnQuotations() throws Exception {
        // Given: A single-day date range
        LocalDate singleDate = LocalDate.now().plusDays(7);
        
        // Create quotation for that specific date
        createAndSaveQuotation("Q-2024-001", singleDate);

        // When & Then: Request upcoming follow-ups for a single day
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", singleDate.toString())
                .param("endDate", singleDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].followUpDate").value(singleDate.toString()));
    }

    /**
     * Test Case 14: Test date range validation - start date and end date are validated
     * Validates: Requirements 8.7
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpcomingFollowups_WithValidDates_ShouldAcceptRequest() throws Exception {
        // Given: Valid start and end dates
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);

        // When & Then: Request should be accepted with valid dates
        mockMvc.perform(get("/api/quotations/upcoming-followups")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Helper method to create and save a single quotation with specified follow-up date
     */
    private Quotation createAndSaveQuotation(String quotationNumber, LocalDate followUpDate) {
        Quotation quotation = new Quotation();
        quotation.setQuotationNumber(quotationNumber);
        quotation.setCompanyId(1L);
        quotation.setStatus(Quotation.QuotationStatus.APPROVED);
        quotation.setTotalAmount(new BigDecimal("1000.00"));
        quotation.setFollowUpDate(followUpDate);
        quotation.setCreatedBy(1L);
        quotation.setCreatedAt(LocalDateTime.now());
        quotation.setRevisionNumber(1);
        
        // Create a simple item for the quotation
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(10);
        item.setUnitPrice(new BigDecimal("100.00"));
        item.setTotalPrice(new BigDecimal("1000.00"));
        items.add(item);
        
        return quotationService.createQuotation(quotation, items);
    }

    /**
     * Helper method to create and save multiple quotations with follow-up dates within a range
     */
    private void createAndSaveQuotations(int count, LocalDate startDate, LocalDate endDate) {
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        
        for (int i = 0; i < count; i++) {
            String quotationNumber = "Q-2024-" + String.format("%03d", i + 1);
            
            // Distribute follow-up dates evenly across the range
            long daysOffset = (daysBetween * i) / count;
            LocalDate followUpDate = startDate.plusDays(daysOffset);
            
            createAndSaveQuotation(quotationNumber, followUpDate);
        }
    }
}
