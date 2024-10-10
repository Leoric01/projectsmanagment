package com.leoric.services;

import com.leoric.models.User;
import com.leoric.response.DTOs.UserProfileResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User findUserProfileByJwt(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;

    User findUserById(Long userId) throws Exception;

    User updateUsersProjectSize(User user, int number) throws Exception;

    UserProfileResponseDTO getUserProfile(User user);
}
