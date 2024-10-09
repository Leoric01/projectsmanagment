package com.leoric.services;

import com.leoric.models.Invitation;
import com.leoric.repositories.InvitationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    @Override
    public void sendInvitation(String targetEmail, Long projectId) throws MessagingException {
        String invitationToken = UUID.randomUUID().toString();
        Invitation invitation = new Invitation();
        invitation.setEmail(targetEmail);
        invitation.setProjectId(projectId);
        invitation.setToken(invitationToken);
        invitationRepository.save(invitation);
        String invitationLink = "http://localhost:5173/accept_invitation?token=" + invitationToken;
        emailService.sendEmailWithToken(targetEmail, invitationLink);
    }

    @Override
    public Invitation acceptInvitation(String token, Long userId) throws Exception {
        Invitation invitation = invitationRepository.findByToken(token);
        if (invitation == null) {
            throw new Exception("Invalid invitation token");
        }

        return invitation;
    }

    @Override
    public String getTokenByUserMail(String userEmail) throws Exception {
        Invitation invitation = invitationRepository.findByEmail(userEmail);
        if (invitation == null) {
            throw new Exception("Invitation not found");
        }
        return invitation.getToken();
    }

    @Override
    public void deleteToken(String token) {
        Invitation invitation = invitationRepository.findByToken(token);
        invitationRepository.delete(invitation);
    }
}
