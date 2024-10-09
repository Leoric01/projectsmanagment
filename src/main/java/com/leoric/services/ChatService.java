package com.leoric.services;

import com.leoric.models.Chat;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    Chat createChat(Chat chat);
}
