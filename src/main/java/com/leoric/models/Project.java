package com.leoric.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    private String name;
    private String description;
    private String category;
    private String status = "Just Created";
    private String deployedUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Chat chat;

    @ManyToOne
    private User owner;

    @Getter
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Issue> issues = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_team_members",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )

    private Set<User> teamMembers = new HashSet<>();

    public void removeIssue(Issue issue) {
        this.issues.remove(issue);
        issue.setProject(null); // Ensure that issue's reference to project is cleared
    }

    public Set<User> getTeamMembers() {
        Set<User> users = new HashSet<>(this.teamMembers);
        users.remove(this.owner);
        return users;
    }

    public Chat getChat() {
        if (chat == null) {
            return new Chat();
        }
        return chat;
    }


    public void addTeamMember(User user) {
        this.teamMembers.add(user);
        user.getProjects().add(this);
    }

    public void removeTeamMember(User user) {
        this.teamMembers.remove(user);
        user.getProjects().remove(this);
    }

}
