package com.leoric.services;

import com.leoric.models.Chat;
import com.leoric.models.Project;
import com.leoric.models.User;
import com.leoric.repositories.ChatRepository;
import com.leoric.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    @Override
    public Project createProject(Project project, User user) throws Exception {
        Project createdProject = new Project();
        createdProject.setOwner(user);
        createdProject.setTags(project.getTags());
        createdProject.setName(project.getName());
        createdProject.setDescription(project.getDescription());
        createdProject.setCategory(project.getCategory());
        createdProject.getTeam().add(user);
        Project savedProject = projectRepository.save(createdProject);
        Chat chat = new Chat();
        chat.setProject(savedProject);
        Chat projectChat = chatRepository.save(chat);
        savedProject.setChat(projectChat);
        return savedProject;
    }

    @Override
    public List<Project> getAllProjects() throws Exception {
        return List.of();
    }

    @Override
    public List<Project> getProjectsByTeam(User user, String category, String tag) throws Exception {
        List<Project> projects = projectRepository.findByTeamContainingOrOwner(user, user);
        if (category != null && !category.isEmpty()) {
            projects = projects.stream()
                    .filter(project -> project.getCategory().equals(category))
                    .toList();
        }
        if (tag != null && !tag.isEmpty()) {
            projects = projects.stream()
                    .filter(project -> project.getTags().contains(tag))
                    .toList();
        }
        return projects;
    }

    @Override
    public Project getProjectById(Long projectId) throws Exception {
        return projectRepository.findById(projectId).orElseThrow(() -> new BadCredentialsException("Project not found"));
    }

    @Override
    public Project updateProject(Project updatedProject, Long id) throws Exception {
        Project project = getProjectById(id);
        BeanUtils.copyProperties(updatedProject, project, "id", "owner", "chat", "issues", "team");
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId) throws Exception {
        projectRepository.deleteById(projectId);
    }

    @Override
    public void deleteProject(Long projectId, Long userId) throws Exception {
        Project projectToDelete = getProjectById(projectId);
        if (projectToDelete.getOwner().getId().equals(userId)) {
            projectRepository.delete(projectToDelete);
            log.info("seems like you are owner, project was deleted");
        } else {
            throw new BadCredentialsException("You are not owner of this project");
        }
    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws Exception {
        Project project = getProjectById(projectId);
        boolean isAlreadyMember = project.getTeam().stream()
                .anyMatch(m -> m.getId().equals(userId));
        if (isAlreadyMember) {
            throw new IllegalArgumentException("This user is already in the team.");
        }
        User user = userService.findUserById(userId);
        project.getTeam().add(user);
        project.getChat().getUsers().add(user);
        projectRepository.save(project);
    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);
        boolean isMember = project.getTeam().stream()
                .anyMatch(m -> m.getId().equals(userId));
        if (!isMember) {
            throw new IllegalArgumentException("This user is not a member of the team.");
        }
        project.getTeam().remove(user);
        project.getChat().getUsers().remove(user);
        projectRepository.save(project);
    }


    @Override
    public Chat getChatByProjectId(Long projectId) throws Exception {
        return getProjectById(projectId).getChat();
    }

    @Override
    public List<Project> searchProjects(String keyword, User user) throws Exception {
        return projectRepository.findByNameContainingAndTeamContains(keyword, user);
    }
}
