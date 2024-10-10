package com.leoric.services;

import com.leoric.models.Chat;
import com.leoric.models.Project;
import com.leoric.models.User;
import com.leoric.response.DTOs.ChatResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    Project createProject(Project project, User user) throws Exception;

    List<Project> getAllProjects() throws Exception;

    List<Project> getProjectsByTeam(User user, String category, String tag) throws Exception;

    Project getProjectById(Long projectId) throws Exception;

    Project updateProject(Project project, Long id) throws Exception;

    void deleteProject(Long projectId) throws Exception;

    void deleteProject(Long projectId, Long userId) throws Exception;

    void addUserToProject(Long projectId, Long userId) throws Exception;

    void removeUserFromProject(Long projectId, Long userId) throws Exception;

    Chat getChatByProjectId(Long projectId) throws Exception;

    List<Project> searchProjects(String keyword, User user) throws Exception;

    List<Project> getUsersProjects(Long userId);

    ChatResponseDto getChatResponseDtoByProjectId(Long projectId) throws Exception;

    Project addNewTeamMember(Long projectId, Long newMemberUserId) throws Exception;
}
