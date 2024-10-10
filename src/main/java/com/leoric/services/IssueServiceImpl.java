package com.leoric.services;

import com.leoric.models.Issue;
import com.leoric.models.Project;
import com.leoric.models.User;
import com.leoric.repositories.IssueRepository;
import com.leoric.requests.IssueRequest;
import com.leoric.response.DTOs.AllIssueDTO;
import com.leoric.response.DTOs.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final ProjectService projectService;
    private final UserService userService;

    @Override
    public Issue getIssueById(Long issueId) throws Exception {
        Optional<Issue> issue = issueRepository.findById(issueId);
        if (issue.isPresent()) {
            return issue.get();
        }
        throw new Exception("Issue " + issueId + " not found");
    }

    @Override
    public List<Issue> getIssuesByProjectId(Long projectId) throws Exception {
        return issueRepository.findByProjectId(projectId);
    }

    @Override
    public Issue createIssue(IssueRequest issueRequest, User user) throws Exception {
        Project project = projectService.getProjectById(issueRequest.getProjectId());
        Issue newIssue = new Issue();
        newIssue.setTitle(issueRequest.getTitle());
        newIssue.setDescription(issueRequest.getDescription());
        newIssue.setStatus(issueRequest.getStatus());
        newIssue.setProjectID(issueRequest.getProjectId());
        newIssue.setDueDate(issueRequest.getDueDate());
        newIssue.setPriority(issueRequest.getPriority());
        newIssue.setProject(project);
        return issueRepository.save(newIssue);
    }

    @Override
    public Optional<Issue> updateIssue(Long issueId, IssueRequest updatedIssue, Long userId) throws Exception {
        return Optional.empty();
    }

    @Override
    public void deleteIssue(Long issueId, Long userId) throws Exception {
        getIssueById(issueId);
        issueRepository.deleteById(issueId);
    }

    @Override
    public List<Issue> getIssuesByAssigneeId(Long assigneeId) throws Exception {
        return List.of();
    }

    @Override
    public List<Issue> searchIssue(String title, String status, String priority, Long assigneeId) throws Exception {
        return List.of();
    }

    @Override
    public List<User> getAssigneeForIssue(Long issueId) throws Exception {
        return List.of();
    }

    @Override
    public Issue addUserToIssue(Long issueId, Long userId) throws Exception {
        User user = userService.findUserById(userId);
        Issue issue = getIssueById(issueId);
        issue.setAssignee(user);
        return issueRepository.save(issue);
    }

    @Override
    public Issue updateStatus(Long issueId, String status) throws Exception {
        Issue issue = getIssueById(issueId);
        issue.setStatus(status);
        return issueRepository.save(issue);
    }

    @Override
    public List<AllIssueDTO> getAllIssues() {
        List<Issue> issues = issueRepository.findAll();
        return issues.stream()
                .map(issue -> {
                    AllIssueDTO issueDto = new AllIssueDTO();
                    issueDto.setId(issue.getId());
                    issueDto.setTitle(issue.getTitle());
                    issueDto.setDescription(issue.getDescription());
                    issueDto.setStatus(issue.getStatus());
                    issueDto.setPriority(issue.getPriority());
                    issueDto.setDueDate(issue.getDueDate());
                    issueDto.setCreatedDate(issue.getCreated());
                    issueDto.setProjectID(issue.getProject() != null ? issue.getProject().getId() : null);
                    issueDto.setProjectName(issue.getProject() != null ? issue.getProject().getName() : null);
                    issueDto.setTags(issue.getTags());
                    if (issue.getAssignee() != null) {
                        UserResponseDTO assigneeDto = issue.getAssignee();
                        issueDto.setAssignee(assigneeDto);
                    } else {
                        issueDto.setAssignee(null);
                    }
                    return issueDto;
                })
                .toList();
    }
}
