package com.leoric.services;

import com.leoric.models.Chat;
import com.leoric.models.Project;
import com.leoric.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Override
    public Project createProject(Project project, User user) throws Exception {
        return null;
    }

    @Override
    public List<Project> getAllProjects() throws Exception {
        return List.of();
    }

    @Override
    public List<Project> getProjectsByTeam(User user, String category, String tag) throws Exception {
        return List.of();
    }

    @Override
    public Project getProjectById(Long projectId) throws Exception {
        return null;
    }

    @Override
    public Project updateProject(Project project, Long userId) throws Exception {
        return null;
    }

    @Override
    public void deleteProject(Long projectId) throws Exception {

    }

    @Override
    public void deleteProject(Long projectId, Long userId) throws Exception {

    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws Exception {

    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) throws Exception {

    }

    @Override
    public Chat getChatByProjectId(Long projectId) throws Exception {
        return null;
    }
}
