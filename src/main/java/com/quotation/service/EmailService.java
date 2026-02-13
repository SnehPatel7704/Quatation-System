package com.quotation.service;

import com.quotation.model.Quotation;
import com.quotation.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private static final String FROM_EMAIL = "noreply@quotationsystem.com";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendQuotationToAdmin(Quotation quotation, String adminEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject("Quotation Ready for Review: " + quotation.getQuotationNumber());
        message.setText("A new quotation " + quotation.getQuotationNumber() + 
                       " has been created and is ready for your review.\n\n" +
                       "Total Amount: " + quotation.getTotalAmount() + "\n" +
                       "Status: " + quotation.getStatus());
        
        mailSender.send(message);
    }

    public void sendQuotationToClient(Quotation quotation, String clientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(clientEmail);
        message.setSubject("Your Quotation: " + quotation.getQuotationNumber());
        message.setText("Dear Customer,\n\n" +
                       "Please find your quotation details below:\n\n" +
                       "Quotation Number: " + quotation.getQuotationNumber() + "\n" +
                       "Total Amount: " + quotation.getTotalAmount() + "\n\n" +
                       "Thank you for your business.");
        
        mailSender.send(message);
    }


    /**
     * Send approval notification to quotation creator
     *
     * @param quotation The approved quotation
     * @param creator The user who created the quotation
     */
    public void sendApprovalNotification(Quotation quotation, User creator) {
        try {
            String htmlContent = loadEmailTemplate("quotation-approval-email.html");

            // Replace placeholders
            htmlContent = htmlContent.replace("${quotationNumber}", quotation.getQuotationNumber());
            htmlContent = htmlContent.replace("${approvalDate}",
                quotation.getUpdatedAt().format(DATE_FORMATTER));
            htmlContent = htmlContent.replace("${approverName}",
                quotation.getApproverName() != null ? quotation.getApproverName() : "Admin");
            htmlContent = htmlContent.replace("${quotationLink}",
                "http://localhost:3000/quotations/" + quotation.getId());

            sendHtmlEmail(creator.getEmail(),
                "Quotation Approved: " + quotation.getQuotationNumber(),
                htmlContent);

        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to send approval notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Send rejection notification to quotation creator
     *
     * @param quotation The rejected quotation
     * @param creator The user who created the quotation
     * @param rejectionReason The reason for rejection
     * @param revisionId The ID of the newly created revision
     */
    public void sendRejectionNotification(Quotation quotation, User creator,
                                         String rejectionReason, Long revisionId) {
        try {
            String htmlContent = loadEmailTemplate("quotation-rejection-email.html");

            // Replace placeholders
            htmlContent = htmlContent.replace("${quotationNumber}", quotation.getQuotationNumber());
            htmlContent = htmlContent.replace("${rejectionDate}",
                quotation.getUpdatedAt().format(DATE_FORMATTER));
            htmlContent = htmlContent.replace("${rejectorName}", "Admin");
            htmlContent = htmlContent.replace("${rejectionReason}", rejectionReason);
            htmlContent = htmlContent.replace("${revisionNumber}",
                String.valueOf(quotation.getRevisionNumber() + 1));
            htmlContent = htmlContent.replace("${revisionLink}",
                "http://localhost:3000/quotations/" + revisionId);

            sendHtmlEmail(creator.getEmail(),
                "Quotation Rejected: " + quotation.getQuotationNumber(),
                htmlContent);

        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to send rejection notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load email template from resources
     */
    private String loadEmailTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + templateName);
        return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
    }

    /**
     * Send HTML email
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(FROM_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

}
