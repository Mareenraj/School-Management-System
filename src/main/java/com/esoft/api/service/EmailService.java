package com.esoft.api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOtpEmail(String toEmail, String otp, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("SMS - Email Verification OTP");
            helper.setText(buildOtpEmailBody(otp, userName), true);

            mailSender.send(message);
            log.info("OTP email sent to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    private String buildOtpEmailBody(String otp, String userName) {
        return """
                <div style="font-family: 'Segoe UI', Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 32px; background: #f8f9fa; border-radius: 12px;">
                    <h2 style="color: #1a1a2e; margin-bottom: 8px;">Email Verification</h2>
                    <p style="color: #555; font-size: 15px;">Hello <strong>%s</strong>,</p>
                    <p style="color: #555; font-size: 15px;">Use the following OTP to verify your email address. This code expires in <strong>5 minutes</strong>.</p>
                    <div style="text-align: center; margin: 28px 0;">
                        <span style="display: inline-block; font-size: 32px; font-weight: 700; letter-spacing: 8px; color: #1a1a2e; background: #e8eaed; padding: 16px 32px; border-radius: 8px;">%s</span>
                    </div>
                    <p style="color: #888; font-size: 13px;">If you didn't request this, please ignore this email.</p>
                    <hr style="border: none; border-top: 1px solid #e0e0e0; margin: 24px 0;">
                    <p style="color: #aaa; font-size: 12px; text-align: center;">School Management System</p>
                </div>
                """.formatted(userName, otp);
    }
}
