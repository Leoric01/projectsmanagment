package com.leoric.services;

import com.leoric.models.Chat;
import com.leoric.models.Message;
import com.leoric.models.User;
import com.leoric.repositories.MessageRepository;
import com.leoric.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;

    @Override
    public Message sendMessage(Long senderId, Long projectId, String content) throws Exception {
        User user = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = projectService.getProjectById(projectId).getChat();
        Message message = new Message();
        message.setContent(content);
        message.setSender(user);
        message.setChat(chat);
        message.setCreatedAt(LocalDateTime.now());
        Message savedMsg = messageRepository.save(message);
        chat.getMessages().add(savedMsg);
        return savedMsg;
    }

    @Override
    public List<Message> getMessagesByProjectId(Long projectId) throws Exception {
        Chat chat = projectService.getChatByProjectId(projectId);
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chat.getId());
    }

}
