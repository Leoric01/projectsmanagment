package com.leoric.response.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllIssueDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long projectID;
    private String projectName;
    private String priority;
    private LocalDate dueDate;
    private LocalDate createdDate;
    private List<String> tags = new ArrayList<>();
    private UserResponseDTO assignee;
    private UserResponseDTO reporter;
}

