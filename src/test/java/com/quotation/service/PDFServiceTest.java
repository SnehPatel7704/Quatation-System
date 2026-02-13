package com.quotation.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.quotation.model.Company;
import com.quotation.model.Product;
import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PDFService
 * Tests PDF generation with various quotations and validates content completeness
 * 
 * Requirements tested: 13.2, 13.3, 13.4
 */
@ExtendWith(MockitoExtension.class)
class PDFServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private PDFService pdfService;

    @BeforeEach
    void setUp() {
        pdfService = new PDFService(jdbcTemplate);
    }

    /**
     * Test Case 1: Test PDF generation with a simple quotation
     * Validates: Requirements 13.2, 13.3
     */
    @Test
    void testGenerateQuotationPDF_WithSimpleQuotation_ShouldGeneratePDF() throws Exception {
        // Given: A simple quotation with one item
        Quotation quotation = createTestQuotation("Q-2024-001", BigDecimal.valueOf(1000.00));
        List<QuotationItem> items = createTestItems(1);
        Company company = createTestCompany("Test Company Inc.");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should be generated successfully
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);
        
        // Verify PDF content
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("QUOTATION");
        assertThat(pdfContent).contains("Q-2024-001");
        assertThat(pdfContent).contains("Test Company Inc.");
        assertThat(pdfContent).contains("1000.0");
    }

    /**
     * Test Case 2: Test PDF generation with multiple items
     * Validates: Requirements 13.2, 13.3
     */
    @Test
    void testGenerateQuotationPDF_WithMultipleItems_ShouldIncludeAllItems() throws Exception {
        // Given: A quotation with multiple items
        Quotation quotation = createTestQuotation("Q-2024-002", BigDecimal.valueOf(5500.00));
        List<QuotationItem> items = createTestItems(5);
        Company company = createTestCompany("Multi-Item Corp");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain all items
        String pdfContent = extractTextFromPDF(pdfBytes);
        
        // Verify all product names are present
        for (int i = 0; i < items.size(); i++) {
            assertThat(pdfContent).contains("Product " + (i + 1));
        }
        
        // Verify total amount
        assertThat(pdfContent).contains("5500.0");
    }

    /**
     * Test Case 3: Test that all required company information is included
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_ShouldIncludeAllCompanyInformation() throws Exception {
        // Given: A quotation with complete company information
        Quotation quotation = createTestQuotation("Q-2024-003", BigDecimal.valueOf(2000.00));
        List<QuotationItem> items = createTestItems(2);
        Company company = createTestCompany("Complete Info Company");
        company.setAddress("123 Business Street, Suite 100, New York, NY 10001");
        company.setEmail("contact@completeinfo.com");
        company.setPhone("+1-555-123-4567");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain all company information
        String pdfContent = extractTextFromPDF(pdfBytes);
        
        assertThat(pdfContent).contains("Complete Info Company");
        assertThat(pdfContent).contains("123 Business Street, Suite 100, New York, NY 10001");
        assertThat(pdfContent).contains("contact@completeinfo.com");
        assertThat(pdfContent).contains("+1-555-123-4567");
    }

    /**
     * Test Case 4: Test that quotation number is included
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_ShouldIncludeQuotationNumber() throws Exception {
        // Given: A quotation with a specific quotation number
        Quotation quotation = createTestQuotation("Q-2024-999", BigDecimal.valueOf(1500.00));
        List<QuotationItem> items = createTestItems(1);
        Company company = createTestCompany("Test Company");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain the quotation number
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("Quotation Number");
        assertThat(pdfContent).contains("Q-2024-999");
    }

    /**
     * Test Case 5: Test that creation date is included
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_ShouldIncludeCreationDate() throws Exception {
        // Given: A quotation with a specific creation date
        LocalDateTime createdAt = LocalDateTime.of(2024, 3, 15, 10, 30);
        Quotation quotation = createTestQuotation("Q-2024-004", BigDecimal.valueOf(1000.00));
        quotation.setCreatedAt(createdAt);
        List<QuotationItem> items = createTestItems(1);
        Company company = createTestCompany("Test Company");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain the formatted date
        String pdfContent = extractTextFromPDF(pdfBytes);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String expectedDate = createdAt.format(formatter);
        
        assertThat(pdfContent).contains("Date");
        assertThat(pdfContent).contains(expectedDate);
    }

    /**
     * Test Case 6: Test that status is included
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_ShouldIncludeStatus() throws Exception {
        // Given: A quotation with APPROVED status
        Quotation quotation = createTestQuotation("Q-2024-005", BigDecimal.valueOf(1000.00));
        quotation.setStatus(Quotation.QuotationStatus.APPROVED);
        List<QuotationItem> items = createTestItems(1);
        Company company = createTestCompany("Test Company");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain the status
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("Status");
        assertThat(pdfContent).contains("APPROVED");
    }

    /**
     * Test Case 7: Test that item details are included (product, quantity, unit price, total)
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_ShouldIncludeItemDetails() throws Exception {
        // Given: A quotation with specific item details
        Quotation quotation = createTestQuotation("Q-2024-006", BigDecimal.valueOf(2500.00));
        
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(25);
        item.setUnitPrice(BigDecimal.valueOf(100.00));
        item.setTotalPrice(BigDecimal.valueOf(2500.00));
        
        List<QuotationItem> items = List.of(item);
        Company company = createTestCompany("Test Company");
        
        // Mock product query
        Product product = new Product();
        product.setId(1L);
        product.setName("Premium Widget");
        product.setDescription("High-quality widget for professional use");
        product.setBasePrice(BigDecimal.valueOf(100.00));
        
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(1L)
        )).thenReturn(product);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain all item details
        String pdfContent = extractTextFromPDF(pdfBytes);
        
        assertThat(pdfContent).contains("Premium Widget");
        // Description may be split across lines in PDF, so check for key words
        assertThat(pdfContent).contains("High-quality");
        assertThat(pdfContent).contains("widget");
        assertThat(pdfContent).contains("25"); // quantity
        assertThat(pdfContent).contains("100.0"); // unit price
        assertThat(pdfContent).contains("2500.0"); // total price
    }

    /**
     * Test Case 8: Test that total amount is included
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_ShouldIncludeTotalAmount() throws Exception {
        // Given: A quotation with a specific total amount
        Quotation quotation = createTestQuotation("Q-2024-007", BigDecimal.valueOf(12345.67));
        List<QuotationItem> items = createTestItems(3);
        Company company = createTestCompany("Test Company");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain the total amount
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("Total Amount");
        assertThat(pdfContent).contains("12345.67");
    }

    /**
     * Test Case 9: Test professional formatting with table headers
     * Validates: Requirements 13.4
     */
    @Test
    void testGenerateQuotationPDF_ShouldIncludeTableHeaders() throws Exception {
        // Given: A quotation with items
        Quotation quotation = createTestQuotation("Q-2024-008", BigDecimal.valueOf(1000.00));
        List<QuotationItem> items = createTestItems(2);
        Company company = createTestCompany("Test Company");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain table headers
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("Product");
        assertThat(pdfContent).contains("Description");
        assertThat(pdfContent).contains("Quantity");
        assertThat(pdfContent).contains("Unit Price");
        assertThat(pdfContent).contains("Total");
    }

    /**
     * Test Case 10: Test with company having minimal information (no address, email, phone)
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_WithMinimalCompanyInfo_ShouldGeneratePDF() throws Exception {
        // Given: A company with only name (no address, email, phone)
        Quotation quotation = createTestQuotation("Q-2024-009", BigDecimal.valueOf(1000.00));
        List<QuotationItem> items = createTestItems(1);
        Company company = new Company();
        company.setId(1L);
        company.setName("Minimal Info Company");
        // No address, email, or phone set
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should be generated successfully with company name
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);
        
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("Minimal Info Company");
    }

    /**
     * Test Case 11: Test with product having no description
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_WithProductNoDescription_ShouldGeneratePDF() throws Exception {
        // Given: A product with no description
        Quotation quotation = createTestQuotation("Q-2024-010", BigDecimal.valueOf(500.00));
        
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(5);
        item.setUnitPrice(BigDecimal.valueOf(100.00));
        item.setTotalPrice(BigDecimal.valueOf(500.00));
        
        List<QuotationItem> items = List.of(item);
        Company company = createTestCompany("Test Company");
        
        // Mock product with no description
        Product product = new Product();
        product.setId(1L);
        product.setName("No Description Product");
        product.setDescription(null); // No description
        product.setBasePrice(BigDecimal.valueOf(100.00));
        
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(1L)
        )).thenReturn(product);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should be generated successfully
        assertThat(pdfBytes).isNotNull();
        String pdfContent = extractTextFromPDF(pdfBytes);
        // Product name may be split across lines, check for key part
        assertThat(pdfContent).contains("No Description");
    }

    /**
     * Test Case 12: Test with large quotation (many items)
     * Validates: Requirements 13.2, 13.3
     */
    @Test
    void testGenerateQuotationPDF_WithManyItems_ShouldGeneratePDF() throws Exception {
        // Given: A quotation with many items
        Quotation quotation = createTestQuotation("Q-2024-011", BigDecimal.valueOf(50000.00));
        List<QuotationItem> items = createTestItems(20);
        Company company = createTestCompany("Large Order Company");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should be generated successfully
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);
        
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("Q-2024-011");
        assertThat(pdfContent).contains("Large Order Company");
    }

    /**
     * Test Case 13: Test with decimal values in prices
     * Validates: Requirements 13.3
     */
    @Test
    void testGenerateQuotationPDF_WithDecimalPrices_ShouldFormatCorrectly() throws Exception {
        // Given: A quotation with decimal prices
        Quotation quotation = createTestQuotation("Q-2024-012", new BigDecimal("1234.56"));
        
        QuotationItem item = new QuotationItem();
        item.setProductId(1L);
        item.setQuantity(3);
        item.setUnitPrice(new BigDecimal("411.52"));
        item.setTotalPrice(new BigDecimal("1234.56"));
        
        List<QuotationItem> items = List.of(item);
        Company company = createTestCompany("Test Company");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain properly formatted decimal values
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("411.52");
        assertThat(pdfContent).contains("1234.56");
    }

    /**
     * Test Case 14: Test error handling when product query fails
     * Validates: Requirements 13.2
     */
    @Test
    void testGenerateQuotationPDF_WhenProductQueryFails_ShouldThrowException() {
        // Given: A quotation with items, but product query will fail
        Quotation quotation = createTestQuotation("Q-2024-013", BigDecimal.valueOf(1000.00));
        List<QuotationItem> items = createTestItems(1);
        Company company = createTestCompany("Test Company");
        
        // Mock product query to throw exception
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                anyLong()
        )).thenThrow(new RuntimeException("Database error"));

        // When & Then: Should throw RuntimeException
        assertThatThrownBy(() -> pdfService.generateQuotationPDF(quotation, items, company))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to generate PDF");
    }

    /**
     * Test Case 15: Test that PDF contains footer message
     * Validates: Requirements 13.4
     */
    @Test
    void testGenerateQuotationPDF_ShouldIncludeFooter() throws Exception {
        // Given: A quotation
        Quotation quotation = createTestQuotation("Q-2024-014", BigDecimal.valueOf(1000.00));
        List<QuotationItem> items = createTestItems(1);
        Company company = createTestCompany("Test Company");
        
        mockProductQuery(items);

        // When: Generate PDF
        byte[] pdfBytes = pdfService.generateQuotationPDF(quotation, items, company);

        // Then: PDF should contain footer message
        String pdfContent = extractTextFromPDF(pdfBytes);
        assertThat(pdfContent).contains("Thank you for your business!");
    }

    // Helper methods

    /**
     * Create a test quotation with specified number and total amount
     */
    private Quotation createTestQuotation(String quotationNumber, BigDecimal totalAmount) {
        Quotation quotation = new Quotation();
        quotation.setId(1L);
        quotation.setQuotationNumber(quotationNumber);
        quotation.setCompanyId(1L);
        quotation.setStatus(Quotation.QuotationStatus.APPROVED);
        quotation.setTotalAmount(totalAmount);
        quotation.setCreatedBy(1L);
        quotation.setCreatedAt(LocalDateTime.now());
        quotation.setRevisionNumber(1);
        return quotation;
    }

    /**
     * Create a test company with specified name
     */
    private Company createTestCompany(String name) {
        Company company = new Company();
        company.setId(1L);
        company.setName(name);
        company.setAddress("123 Test Street");
        company.setEmail("test@example.com");
        company.setPhone("+1-555-0000");
        return company;
    }

    /**
     * Create a list of test quotation items
     */
    private List<QuotationItem> createTestItems(int count) {
        List<QuotationItem> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            QuotationItem item = new QuotationItem();
            item.setId((long) (i + 1));
            item.setProductId((long) (i + 1));
            item.setQuantity(10);
            item.setUnitPrice(BigDecimal.valueOf(100.00));
            item.setTotalPrice(BigDecimal.valueOf(1000.00));
            items.add(item);
        }
        return items;
    }

    /**
     * Mock the product query for a list of items
     */
    private void mockProductQuery(List<QuotationItem> items) {
        for (QuotationItem item : items) {
            Product product = new Product();
            product.setId(item.getProductId());
            product.setName("Product " + item.getProductId());
            product.setDescription("Description for product " + item.getProductId());
            product.setBasePrice(item.getUnitPrice());
            
            when(jdbcTemplate.queryForObject(
                    anyString(),
                    any(RowMapper.class),
                    eq(item.getProductId())
            )).thenReturn(product);
        }
    }

    /**
     * Extract text content from PDF bytes for verification
     */
    private String extractTextFromPDF(byte[] pdfBytes) throws Exception {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(pdfBytes);
             PdfReader reader = new PdfReader(bais);
             PdfDocument pdfDoc = new PdfDocument(reader)) {
            
            StringBuilder text = new StringBuilder();
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                text.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i)));
            }
            return text.toString();
        }
    }
}
