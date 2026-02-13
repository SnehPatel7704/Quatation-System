package com.quotation.controller;

import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import com.quotation.repository.QuotationRepository;
import com.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for approval endpoints in QuotationController.
 * Tests the approval workflow including approval, rejection, and revision history.
 * 
 * Requirements tested: 6.2, 6.4, 6.13, 6.14
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
public class QuotationControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private QuotationRepository quotationRepository;

    private Quotation testQuotation;

    @BeforeEach
    public void setUp() {
        // Set up MockMvc with Spring Security
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Create a test quotation with PENDING_APPROVAL status
        testQuotation = new Quotation();
        testQuotation.setQuotationNumber("Q-TEST-001");
        testQuotation.setCompanyId(1L);
        testQuotation.setStatus(Quotation.QuotationStatus.PENDING_APPROVAL);
        testQuotation.setTotalAmount(new BigDecimal("1000.00"));
        testQuotation.setCreatedBy(1L);
        testQuotation.setCreatedAt(LocalDateTime.now());
        testQuotation.setRevisionNumber(1);

        // Create test items
        List<QuotationItem> items = new ArrayList<>();
        QuotationItem item1 = new QuotationItem();
        item1.setProductId(1L);
        item1.setQuantity(10);
        item1.setUnitPrice(new BigDecimal("50.00"));
        item1.setTotalPrice(new BigDecimal("500.00"));
        items.add(item1);

        QuotationItem item2 = new QuotationItem();
        item2.setProductId(2L);
        item2.setQuantity(5);
        item2.setUnitPrice(new BigDecimal("100.00"));
        item2.setTotalPrice(new BigDecimal("500.00"));
        items.add(item2);

        // Save quotation with items
        testQuotation = quotationService.createQuotation(testQuotation, items);
    }

    /**
     * Test Case 1: Test approval with ADMIN role (should succeed)
     * Validates: Requirements 6.2, 6.13
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testApprovalWithAdminRole_ShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/quotations/{id}/approve", testQuotation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.approvedBy").value(notNullValue()))
                .andExpect(jsonPath("$.id").value(testQuotation.getId()));
    }

    /**
     * Test Case 2: Test approval with USER role (should fail with 403)
     * Validates: Requirements 6.13, 6.14
     */
    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    public void testApprovalWithUserRole_ShouldFail() throws Exception {
        mockMvc.perform(post("/api/quotations/{id}/approve", testQuotation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    /**
     * Test Case 3: Test rejection with reason (should succeed)
     * Validates: Requirements 6.4, 6.13
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testRejectionWithReason_ShouldSucceed() throws Exception {
        String rejectionReason = "Pricing needs to be revised";

        mockMvc.perform(post("/api/quotations/{id}/reject", testQuotation.getId())
                .param("rejectionReason", rejectionReason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.revisionNumber").value(2))
                .andExpect(jsonPath("$.parentQuotationId").value(testQuotation.getId()));

        // Verify the original quotation was marked as REJECTED
        Quotation originalQuotation = quotationRepository.findById(testQuotation.getId()).orElseThrow();
        assert originalQuotation.getStatus() == Quotation.QuotationStatus.REJECTED;
        assert originalQuotation.getRejectionReason().equals(rejectionReason);
    }

    /**
     * Test Case 4: Test rejection without reason (should fail with 400)
     * Validates: Requirements 6.4
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testRejectionWithoutReason_ShouldFail() throws Exception {
        mockMvc.perform(post("/api/quotations/{id}/reject", testQuotation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test Case 5: Test getting revision history
     * Validates: Requirements 6.14
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetRevisionHistory_ShouldReturnRevisions() throws Exception {
        // First, reject the quotation to create a revision
        String rejectionReason = "Initial rejection for testing";
        quotationService.rejectQuotation(testQuotation.getId(), rejectionReason, 1L);

        // Get revision history
        mockMvc.perform(get("/api/quotations/{id}/revisions", testQuotation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].parentQuotationId").value(testQuotation.getId()))
                .andExpect(jsonPath("$[0].revisionNumber").value(2));
    }

    /**
     * Test Case 6: Test approval of non-PENDING_APPROVAL quotation (should fail)
     * Validates: Requirements 6.2
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testApprovalOfNonPendingQuotation_ShouldFail() throws Exception {
        // First approve the quotation
        quotationService.approveQuotation(testQuotation.getId(), 1L);

        // Try to approve again
        mockMvc.perform(post("/api/quotations/{id}/approve", testQuotation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError()); // Should throw IllegalStateException
    }

    /**
     * Test Case 7: Test rejection with USER role (should fail with 403)
     * Validates: Requirements 6.13, 6.14
     */
    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    public void testRejectionWithUserRole_ShouldFail() throws Exception {
        mockMvc.perform(post("/api/quotations/{id}/reject", testQuotation.getId())
                .param("rejectionReason", "Some reason")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    /**
     * Test Case 8: Test approval with SUPERADMIN role (should succeed)
     * Validates: Requirements 6.13
     */
    @Test
    @WithMockUser(username = "superadmin", authorities = {"SUPERADMIN"})
    public void testApprovalWithSuperAdminRole_ShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/quotations/{id}/approve", testQuotation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.approvedBy").value(notNullValue()));
    }

    /**
     * Test Case 9: Test rejection with SUPERADMIN role (should succeed)
     * Validates: Requirements 6.13
     */
    @Test
    @WithMockUser(username = "superadmin", authorities = {"SUPERADMIN"})
    public void testRejectionWithSuperAdminRole_ShouldSucceed() throws Exception {
        String rejectionReason = "Needs revision";

        mockMvc.perform(post("/api/quotations/{id}/reject", testQuotation.getId())
                .param("rejectionReason", rejectionReason)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.revisionNumber").value(2));
    }

    /**
     * Test Case 10: Test getting revisions for quotation without revisions
     * Validates: Requirements 6.14
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetRevisionHistoryForQuotationWithoutRevisions_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/quotations/{id}/revisions", testQuotation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Test Case 11: Test approval of non-existent quotation (should fail with 500)
     * Validates: Requirements 6.2
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testApprovalOfNonExistentQuotation_ShouldFail() throws Exception {
        Long nonExistentId = 99999L;

        mockMvc.perform(post("/api/quotations/{id}/approve", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    /**
     * Test Case 12: Test rejection of non-existent quotation (should fail with 500)
     * Validates: Requirements 6.4
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testRejectionOfNonExistentQuotation_ShouldFail() throws Exception {
        Long nonExistentId = 99999L;

        mockMvc.perform(post("/api/quotations/{id}/reject", nonExistentId)
                .param("rejectionReason", "Some reason")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}
