package com.leoric.services;

import com.leoric.models.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    Message sendMessage(Long senderId, Long chatId, String message) throws Exception;

    List<Message> getMessagesByProjectId(Long projectId) throws Exception;
}
