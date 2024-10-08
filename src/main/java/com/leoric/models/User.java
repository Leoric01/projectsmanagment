package com.leoric.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String fullName;
    private String email;
    private int projectSize;
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private List<Issue> assignedIssues = new ArrayList<>();

    @ManyToMany
    private List<Project> projects = new ArrayList<>();
}
