package com.leoric.services;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendEmailWithToken(String userEmail, String link) throws MessagingException;
}
