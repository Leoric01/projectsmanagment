package com.leoric.controllers;

import com.leoric.models.Chat;
import com.leoric.models.Invitation;
import com.leoric.models.Project;
import com.leoric.models.User;
import com.leoric.requests.InviteRequest;
import com.leoric.response.MessageResponse;
import com.leoric.services.InvitationService;
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
    private final InvitationService invitationService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.getProjectsByTeam(user, category, tag);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> project = projectService.getAllProjects();
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(
            @RequestHeader(JWT_HEADER) String jwt,
            @PathVariable Long projectId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Project>> getUsersProjects(
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.getUsersProjects(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestHeader(JWT_HEADER) String jwt,
            @RequestBody Project projectIn
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Project createdProject = projectService.createProject(projectIn, user);
        return ResponseEntity.status(HttpStatus.OK).body(createdProject);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(
            @RequestHeader(JWT_HEADER) String jwt,
            @PathVariable Long projectId,
            @RequestBody Project projectIn
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Project createdProject = projectService.updateProject(projectIn, projectId);
        return ResponseEntity.status(HttpStatus.OK).body(createdProject);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(
            @RequestHeader(JWT_HEADER) String jwt,
            @PathVariable Long projectId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        projectService.deleteProject(projectId, user.getId());
        MessageResponse res = new MessageResponse("Project deleted successfuly");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(
            @RequestHeader(JWT_HEADER) String jwt,
            @RequestParam(required = false) String keyword
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.searchProjects(keyword, user);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @GetMapping("/{projectId}/chat")
    public ResponseEntity<Chat> getChatByProjectId(
            @RequestHeader(JWT_HEADER) String jwt,
            @PathVariable Long projectId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Chat chat = projectService.getChatByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(chat);
    }

    @PostMapping("/invite")
    public ResponseEntity<MessageResponse> inviteProject(
            @RequestHeader(JWT_HEADER) String jwt,
            @RequestBody InviteRequest request,
            @RequestBody Project projectIn
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        invitationService.sendInvitation(request.getEmail(), request.getProjectId());
        MessageResponse response = new MessageResponse("Invitation sent successfuly");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/accept_invitation")
    public ResponseEntity<Invitation> acceptInvitation(
            @RequestHeader(JWT_HEADER) String jwt,
            @RequestParam String token,
            @RequestBody Project projectIn
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Invitation invitation = invitationService.acceptInvitation(token, user.getId());
        projectService.addUserToProject(invitation.getProjectId(), projectIn.getId());
        return ResponseEntity.status(HttpStatus.OK).body(invitation);
    }
}
