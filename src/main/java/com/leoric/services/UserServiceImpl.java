package com.leoric.services;

import com.leoric.config.JwtProvider;
import com.leoric.models.Issue;
import com.leoric.models.User;
import com.leoric.repositories.UserRepository;
import com.leoric.response.DTOs.IssueResponseDTO;
import com.leoric.response.DTOs.UserProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserProfileByJwt(String jwt) {
        String email = jwtProvider.getEmailFromToken(jwt);
        return findUserByEmail(email);
    }

    @Override
    // USE TRANSACTIONAL INSTEAD EAGER IN CASE OF PERFORMANCE ISSUES
    public User findUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public User findUserById(Long userId) throws BadCredentialsException {
        return userRepository.findById(userId).orElseThrow(
                () -> new BadCredentialsException("id not found in db"));
    }

    @Override
    public User updateUsersProjectSize(User user, int number) throws Exception {
        user.setProjectSize(user.getProjectSize() + number);

        return userRepository.save(user);
    }

    @Override
    public UserProfileResponseDTO getUserProfile(User user) {
        UserProfileResponseDTO responseDTO = new UserProfileResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getFullName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setProjectSize(user.getProjectSize());
        List<IssueResponseDTO> assignedIssueDTOs = convertIssuesToDTOHelper(user.getAssignedIssues());
        responseDTO.setAssignedIssues(assignedIssueDTOs != null ? assignedIssueDTOs : Collections.emptyList());
        return responseDTO;
    }

    private List<IssueResponseDTO> convertIssuesToDTOHelper(List<Issue> issues) {
        return (issues == null) ? new ArrayList<>() : issues.stream()
                .map(issue -> {
                    IssueResponseDTO dto = new IssueResponseDTO();
                    dto.setId(issue.getId());
                    dto.setTitle(issue.getTitle());
                    dto.setDescription(issue.getDescription());
                    dto.setStatus(issue.getStatus());
                    dto.setProjectID(issue.getProject() != null ? issue.getProject().getId() : null);
                    dto.setProjectName(issue.getProject() != null ? issue.getProject().getName() : null);
                    dto.setPriority(issue.getPriority());
                    dto.setDueDate(issue.getDueDate());
                    dto.setTags(issue.getTags());
                    return dto;
                })
                .toList();
    }
}
