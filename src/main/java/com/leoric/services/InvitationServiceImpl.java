package com.leoric.services;

import com.leoric.models.Invitation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationServiceImpl implements InvitationService {


    @Override
    public void sendInvitation(String targetEmail, Long projectId) {

    }

    @Override
    public Invitation acceptInvitation(String targetEmail, Long projectId) {
        return null;
    }

    @Override
    public String getTokenByUserMail(String userEmail) {
        return "";
    }

    @Override
    public void deleteToken(String token) {

    }
}
