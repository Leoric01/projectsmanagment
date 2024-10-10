package com.leoric.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String fullName;
    private String email;
    private int projectSize;

    @JsonManagedReference
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Issue> assignedIssues = new ArrayList<>();

    @JsonManagedReference
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();
}
