package com.leoric.controllers;

import com.leoric.models.Chat;
import com.leoric.models.Project;
import com.leoric.models.User;
import com.leoric.response.MessageResponse;
import com.leoric.services.ProjectService;
import com.leoric.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.leoric.config.JwtConstant.JWT_HEADER;


@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestHeader(JWT_HEADER) String token
    ) throws Exception {
        User user = userService.findUserProfileByJwt(token);
        List<Project> projects = projectService.getProjectsByTeam(user, category, tag);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(
            @RequestHeader(JWT_HEADER) String token,
            @PathVariable Long projectId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(token);
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestHeader(JWT_HEADER) String token,
            @RequestBody Project projectIn
    ) throws Exception {
        User user = userService.findUserProfileByJwt(token);
        Project createdProject = projectService.createProject(projectIn, user);
        return ResponseEntity.status(HttpStatus.OK).body(createdProject);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(
            @RequestHeader(JWT_HEADER) String token,
            @PathVariable Long projectId,
            @RequestBody Project projectIn
    ) throws Exception {
        User user = userService.findUserProfileByJwt(token);
        Project createdProject = projectService.updateProject(projectIn, projectId);
        return ResponseEntity.status(HttpStatus.OK).body(createdProject);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(
            @RequestHeader(JWT_HEADER) String token,
            @PathVariable Long projectId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(token);
        projectService.deleteProject(projectId, user.getId());
        MessageResponse res = new MessageResponse("Project deleted successfuly");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(
            @RequestHeader(JWT_HEADER) String token,
            @RequestParam(required = false) String keyword
    ) throws Exception {
        User user = userService.findUserProfileByJwt(token);
        List<Project> projects = projectService.searchProjects(keyword, user);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @GetMapping("/{projectId}/chat")
    public ResponseEntity<Chat> getChatByProjectId(
            @RequestHeader(JWT_HEADER) String token,
            @PathVariable Long projectId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(token);
        Chat chat = projectService.getChatByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(chat);
    }
}
