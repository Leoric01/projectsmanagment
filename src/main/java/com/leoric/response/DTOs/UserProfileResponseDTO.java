package com.leoric.response.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO {
    private Long id;
    private String name;
    private String email;
    private int projectSize;
    private List<IssueResponseDTO> assignedIssues;
}
