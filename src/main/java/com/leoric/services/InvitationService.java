package com.leoric.services;


import com.leoric.models.Invitation;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface InvitationService {
    void sendInvitation(String targetEmail, Long projectId) throws MessagingException;

    Invitation acceptInvitation(String token) throws Exception;

    String getTokenByUserMail(String userEmail) throws Exception;

    void deleteToken(String token);
}
