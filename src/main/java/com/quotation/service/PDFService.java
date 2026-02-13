package com.quotation.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.quotation.model.Company;
import com.quotation.model.Product;
import com.quotation.model.Quotation;
import com.quotation.model.QuotationItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PDFService {

    private final JdbcTemplate jdbcTemplate;

    public PDFService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Generate a PDF document for a quotation
     * 
     * @param quotation The quotation to generate PDF for
     * @param items The list of quotation items
     * @param company The company associated with the quotation
     * @return PDF as byte array
     */
    public byte[] generateQuotationPDF(Quotation quotation, List<QuotationItem> items, Company company) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title
            Paragraph title = new Paragraph("QUOTATION")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Add quotation details
            document.add(new Paragraph("Quotation Number: " + quotation.getQuotationNumber())
                    .setFontSize(12)
                    .setBold());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            document.add(new Paragraph("Date: " + quotation.getCreatedAt().format(formatter))
                    .setFontSize(12));
            
            document.add(new Paragraph("Status: " + quotation.getStatus().toString())
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Add company information
            document.add(new Paragraph("Company Information")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10));
            
            document.add(new Paragraph("Company Name: " + company.getName())
                    .setFontSize(11));
            
            if (company.getAddress() != null && !company.getAddress().isEmpty()) {
                document.add(new Paragraph("Address: " + company.getAddress())
                        .setFontSize(11));
            }
            
            if (company.getEmail() != null && !company.getEmail().isEmpty()) {
                document.add(new Paragraph("Email: " + company.getEmail())
                        .setFontSize(11));
            }
            
            if (company.getPhone() != null && !company.getPhone().isEmpty()) {
                document.add(new Paragraph("Phone: " + company.getPhone())
                        .setFontSize(11)
                        .setMarginBottom(20));
            }

            // Add items table
            document.add(new Paragraph("Items")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10)
                    .setMarginBottom(10));

            // Create table with 5 columns: Product, Description, Quantity, Unit Price, Total
            float[] columnWidths = {3, 4, 2, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add table headers
            table.addHeaderCell(new Cell().add(new Paragraph("Product").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Description").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Unit Price").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));

            // Add items to table
            for (QuotationItem item : items) {
                // Fetch product details
                Product product = jdbcTemplate.queryForObject(
                        "SELECT * FROM products WHERE id = ?",
                        (rs, rowNum) -> {
                            Product p = new Product();
                            p.setId(rs.getLong("id"));
                            p.setName(rs.getString("name"));
                            p.setDescription(rs.getString("description"));
                            p.setBasePrice(rs.getBigDecimal("base_price"));
                            return p;
                        },
                        item.getProductId()
                );

                table.addCell(new Cell().add(new Paragraph(product.getName())));
                table.addCell(new Cell().add(new Paragraph(
                        product.getDescription() != null ? product.getDescription() : "")));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity())))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph("OMR " + item.getUnitPrice().toString()))
                        .setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph("OMR " + item.getTotalPrice().toString()))
                        .setTextAlignment(TextAlignment.RIGHT));
            }

            document.add(table);

            // Add total amount
            document.add(new Paragraph("Total Amount: OMR " + quotation.getTotalAmount().toString())
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20));

            // Add footer
            document.add(new Paragraph("Thank you for your business!")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30)
                    .setItalic());

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }
}
