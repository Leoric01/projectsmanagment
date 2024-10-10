package com.leoric.controllers;

import com.leoric.models.User;
import com.leoric.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.leoric.config.JwtConstant.JWT_HEADER;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(
            @RequestHeader(JWT_HEADER) String jwt

    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return ResponseEntity.ok(user);
    }
}
