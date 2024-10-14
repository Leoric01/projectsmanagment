package com.leoric.services;

import com.leoric.models.Issue;
import com.leoric.models.Project;
import com.leoric.models.User;
import com.leoric.repositories.IssueRepository;
import com.leoric.repositories.ProjectRepository;
import com.leoric.requests.IssueRequest;
import com.leoric.response.DTOs.AllIssueDTO;
import com.leoric.response.DTOs.UserResponseDTO;
import jakarta.transaction.Transactional;
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
    private final ProjectRepository projectRepository;

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

        if (issueRequest.getTitle() != null && !issueRequest.getTitle().isBlank()) {
            newIssue.setTitle(issueRequest.getTitle());
        }

        if (issueRequest.getDescription() != null && !issueRequest.getDescription().isBlank()) {
            newIssue.setDescription(issueRequest.getDescription());
        }

        if (issueRequest.getStatus() != null && !issueRequest.getStatus().isBlank()) {
            newIssue.setStatus(issueRequest.getStatus());
        }

        if (issueRequest.getProjectId() != null) {
            newIssue.setProjectID(issueRequest.getProjectId());
        }

        if (issueRequest.getDueDate() != null) {
            newIssue.setDueDate(issueRequest.getDueDate());
        }

        if (issueRequest.getPriority() != null) {
            newIssue.setPriority(issueRequest.getPriority());
        }

        newIssue.setProject(project);

        return issueRepository.save(newIssue);
    }

    @Override
    public Optional<Issue> updateIssue(Long issueId, IssueRequest updatedIssue, Long userId) throws Exception {
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteIssue(Long id, Long userId) throws Exception {
        issueRepository.deleteById(id);
    }

    @Override
    public void deleteIssue(Long issueId) throws Exception {
        Issue issue = getIssueById(issueId);
        Project project = issue.getProject();
        if (project != null) {
            project.removeIssue(issue);
        }
        assert project != null;
        projectRepository.save(project);
        issueRepository.delete(issue);
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
                    if (issue.getReporter() != null) {
                        UserResponseDTO reporterDto = issue.getReporter();
                        issueDto.setReporter(reporterDto);
                    } else {
                        issueDto.setAssignee(null);
                    }
                    return issueDto;
                })
                .toList();
    }
//    private void setIfNotEmpty(Consumer<String> setter, String value) {
//        if (value != null && !value.isBlank()) {
//            setter.accept(value);
//        }
//    }
//    private <T> void setIfNotNull(Consumer<T> setter, T value) {
//        if (value != null) {
//            setter.accept(value);
//        }
//    }
}
