package com.leoric.controllers;

import com.leoric.models.Chat;
import com.leoric.models.Message;
import com.leoric.models.User;
import com.leoric.requests.CreateMessageRequest;
import com.leoric.services.MessageService;
import com.leoric.services.ProjectService;
import com.leoric.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final ProjectService projectService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestBody CreateMessageRequest msgRequest
    ) throws Exception {
        User user = userService.findUserById(msgRequest.getSenderId());
        Chat chat = projectService.getChatByProjectId(msgRequest.getProjectId());
        if (chat == null) {
            throw new Exception("Chat not found");
        }
        Message sentMessage = messageService.sendMessage(
                msgRequest.getSenderId(),
                msgRequest.getProjectId(),
                msgRequest.getContent());
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(
            @PathVariable("projectId") Long projectId
    ) throws Exception {
        List<Message> messages = messageService.getMessagesByProjectId(projectId);
        return ResponseEntity.ok(messages);
    }
}
