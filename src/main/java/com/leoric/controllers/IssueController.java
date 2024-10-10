package com.leoric.controllers;

import com.leoric.models.Issue;
import com.leoric.models.User;
import com.leoric.requests.IssueRequest;
import com.leoric.response.AuthResponse;
import com.leoric.response.IssueDTO;
import com.leoric.services.IssueService;
import com.leoric.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.leoric.config.JwtConstant.JWT_HEADER;

@RestController
@RequestMapping("/api/issue")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final UserService userService;

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long issueId) throws Exception {
        return ResponseEntity.ok(issueService.getIssueById(issueId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Issue>> getIssueByProjectId(@PathVariable Long projectId)
            throws Exception {
        return ResponseEntity.ok(issueService.getIssueByProjectId(projectId));
    }

    @PostMapping
    public ResponseEntity<IssueDTO> createIssue(
            @RequestBody IssueRequest issueReq,
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        System.out.println("issue-----" + issueReq);
        User tokenUser = userService.findUserProfileByJwt(jwt);
        User user = userService.findUserById(tokenUser.getId());
        Issue createdIssue = issueService.createIssue(issueReq, user);
        IssueDTO issueDTO = IssueDTO.builder()
                .id(createdIssue.getId())
                .title(createdIssue.getTitle())
                .description(createdIssue.getDescription())
                .status(createdIssue.getStatus())
                .projectID(createdIssue.getProject().getId())
                .priority(createdIssue.getPriority())
                .dueDate(createdIssue.getDueDate())
                .tags(createdIssue.getTags())
                .project(createdIssue.getProject())
                .assignee(createdIssue.getAssignee())
                .build();
        return ResponseEntity.ok(issueDTO);
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<AuthResponse> deleteIssue(
            @PathVariable Long issueId,
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User tokenUser = userService.findUserProfileByJwt(jwt);
        User user = userService.findUserById(tokenUser.getId());
        issueService.deleteIssue(issueId, user.getId());
        AuthResponse res = new AuthResponse();
        res.setMessage("Issue deleted");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{issueId}/assignee/{userId}")
    public ResponseEntity<Issue> addUserToIssue(
            @PathVariable Long issueId,
            @PathVariable Long userId,
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Issue issue = issueService.addUserToIssue(issueId, userId);
        return ResponseEntity.ok(issue);
    }

    @PutMapping("/{issueId}/")
    public ResponseEntity<Issue> updateIssueStatus(
            @RequestParam String status,
            @PathVariable Long issueId,
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Issue issue = issueService.updateStatus(issueId, status);
        return ResponseEntity.ok(issue);
    }

}
