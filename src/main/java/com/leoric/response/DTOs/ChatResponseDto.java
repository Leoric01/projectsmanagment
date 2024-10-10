package com.leoric.response.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {
    private Long id;
    private String name;
    private Long projectId;
    private String projectName;
    private List<MessageResponseDTO> messages;
    private List<UserResponseDTO> users;
}
