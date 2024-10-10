package com.leoric.controllers;

import com.leoric.models.Issue;
import com.leoric.models.User;
import com.leoric.requests.IssueRequest;
import com.leoric.response.AuthResponse;
import com.leoric.response.DTOs.IssueDTO;
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
        return ResponseEntity.ok(issueService.getIssuesByProjectId(projectId));
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
        IssueDTO issueDTO = new IssueDTO();
        try {
            issueDTO.setId(createdIssue.getId());
            issueDTO.setTitle(createdIssue.getTitle());
            issueDTO.setDescription(createdIssue.getDescription());
            issueDTO.setStatus(createdIssue.getStatus());
            issueDTO.setProjectID(createdIssue.getProject().getId());
            issueDTO.setPriority(createdIssue.getPriority());
            issueDTO.setDueDate(createdIssue.getDueDate());
            issueDTO.setTags(createdIssue.getTags());
            issueDTO.setProject(createdIssue.getProject());
            issueDTO.setAssignee(createdIssue.getAssignee());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return ResponseEntity.ok(issueDTO);
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<AuthResponse> deleteIssue(
            @PathVariable Long issueId,
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        issueService.deleteIssue(issueId);
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
