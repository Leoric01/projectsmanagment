package com.leoric.services;

import com.leoric.models.Chat;
import com.leoric.models.Message;
import com.leoric.models.Project;
import com.leoric.models.User;
import com.leoric.repositories.ChatRepository;
import com.leoric.repositories.ProjectRepository;
import com.leoric.repositories.UserRepository;
import com.leoric.response.DTOs.ChatResponseDto;
import com.leoric.response.DTOs.MessageResponseDTO;
import com.leoric.response.DTOs.UserResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final UserRepository userRepository;

    @Override
    public Project createProject(Project project, User user) throws Exception {
        Project createdProject = new Project();
        createdProject.setOwner(user);
        createdProject.setTags(project.getTags());
        createdProject.setName(project.getName());
        createdProject.setDescription(project.getDescription());
        createdProject.setCategory(project.getCategory());
        createdProject.getTeamMembers().add(user);
        Project savedProject = projectRepository.save(createdProject);
        Chat chat = new Chat();
        chat.setProject(savedProject);
        Chat projectChat = chatRepository.save(chat);
        savedProject.setChat(projectChat);
        return savedProject;
    }

    @Override
    public List<Project> getAllProjects() throws Exception {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> getProjectsByTeam(User user, String category, String tag) throws Exception {
        List<Project> projects = projectRepository.findByTeamMembersContainingOrOwner(user, user);
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
        return projectRepository.findById(projectId).orElseThrow(
                () -> new BadCredentialsException("Project with " + projectId + " id was not found"));
    }

    @Override
    public Project updateProject(Project updatedProject, Long id) throws Exception {
        Project project = getProjectById(id);
        BeanUtils.copyProperties(updatedProject, project, "id", "owner", "chat", "issues", "teamMembers");
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
        boolean isAlreadyMember = project.getTeamMembers().stream()
                .anyMatch(m -> m.getId().equals(userId));
        if (isAlreadyMember) {
            throw new IllegalArgumentException("This user is already in the team.");
        }
        User user = userService.findUserById(userId);
        project.getTeamMembers().add(user);
        project.getChat().getUsers().add(user);
        projectRepository.save(project);
    }

    @Transactional
    @Override
    public Project addNewTeamMember(Long projectId, Long newMemberUserId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(newMemberUserId);
        project.addTeamMember(user);
        user.addProject(project);
        return projectRepository.save(project);
    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);
        boolean isMember = project.getTeamMembers().stream()
                .anyMatch(m -> m.getId().equals(userId));
        if (!isMember) {
            throw new IllegalArgumentException("This user is not a member of the team.");
        }
        project.getTeamMembers().remove(user);
        project.getChat().getUsers().remove(user);
        projectRepository.save(project);
    }


    @Override
    public List<Project> searchProjects(String keyword, User user) throws Exception {
        List<Project> projects = projectRepository.findByNameContainingAndTeamMembersContains(keyword, user);
        if (projects.isEmpty()) {
            throw new Exception("No projects found for the user: " + user.getFullName());
        }
        return projects;
    }

    @Override
    public List<Project> getUsersProjects(Long userId) {
        return projectRepository.findAll().stream()
                .filter(x -> x.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public Chat getChatByProjectId(Long projectId) throws Exception {
        return getProjectById(projectId).getChat();
    }

    @Override
    public ChatResponseDto getChatResponseDtoByProjectId(Long projectId) throws Exception {
        Project project = getProjectById(projectId);
        if (project.getChat() == null) {
            throw new Exception("No chat found for project with id: " + projectId);
        }
        if (project.getChat().getUsers() == null || project.getChat().getMessages() == null) {
            throw new Exception("No chat usesr or messages found for project with id: " + projectId);
        }
        Chat chat = project.getChat();
        List<UserResponseDTO> userDtos = mapUsersToDTO(project.getTeamMembers());
        List<MessageResponseDTO> messagesDtos = mapMessagesToDTO(chat.getMessages());
        return new ChatResponseDto(
                chat.getId(),
                chat.getProject().getName() + " Chat",
                project.getId(),
                project.getName(),
                messagesDtos,
                userDtos
        );
    }

    private List<UserResponseDTO> mapUsersToDTO(Set<User> users) {
        if (users == null || users.isEmpty()) {
            return new ArrayList<>();
        }
        return users.stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getFullName(), user.getEmail()))
                .toList();
    }

    private List<MessageResponseDTO> mapMessagesToDTO(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return new ArrayList<>();
        }
        return messages.stream()
                .map(message -> new MessageResponseDTO(
                        message.getId(),
                        message.getContent(),
                        message.getCreatedAt(),
                        message.getSender().getId(),
                        message.getSender().getFullName()
                ))
                .toList();
    }
}
