package com.leoric.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    private String fullName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;
    private int projectSize;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Issue> assignedIssues = new ArrayList<>();

    //    @JsonManagedReference
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ManyToMany(mappedBy = "teamMembers", fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();

    @OneToOne(mappedBy = "reporter")
    private Issue reportedIssue;

    public void addProject(Project project) {
        this.projects.add(project);
        project.getTeamMembers().add(this);
    }

    public void removeProject(Project project) {
        this.projects.remove(project);
        project.getTeamMembers().remove(this);
    }
}
