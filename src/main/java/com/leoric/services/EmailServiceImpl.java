package com.leoric.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailWithToken(String to, String link) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        String subject = "Join Project Team Invitation";
        String text = "Click to Join Project Team Invitation: " + link;

        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setTo(to);
        try {
            mailSender.send(mimeMessage);
            log.info("Verification email sent to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send verification email due to: {}", e.getMessage());
            throw new MailSendException("Failed to send verification email due to: " + e.getMessage());
        }
    }
}
