package com.leoric.response;

import com.leoric.models.Project;
import com.leoric.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long projectID;
    private String priority;
    private LocalDateTime dueDate;
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    private Project project;
    private User assignee;

}
