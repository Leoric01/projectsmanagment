package com.leoric.services;


import com.leoric.models.Invitation;
import org.springframework.stereotype.Service;

@Service
public interface InvitationService {
    void sendInvitation(String targetEmail, Long projectId);

    Invitation acceptInvitation(String targetEmail, Long projectId);

    String getTokenByUserMail(String userEmail);

    void deleteToken(String token);
}
