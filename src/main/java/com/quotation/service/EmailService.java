package com.quotation.service;

import com.quotation.model.Quotation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

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
}
