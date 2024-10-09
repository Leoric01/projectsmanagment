package com.leoric.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueRequest {
    private String title;
    private String description;
    private String status;
    private Long projectId;
    private String priority;
    private LocalDateTime dueDate;
}
