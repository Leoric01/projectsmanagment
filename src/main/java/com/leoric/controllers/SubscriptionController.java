package com.leoric.controllers;

import com.leoric.models.PlanType;
import com.leoric.models.Subscription;
import com.leoric.models.User;
import com.leoric.services.SubscriptionService;
import com.leoric.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.leoric.config.JwtConstant.JWT_HEADER;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<Subscription> getUserSubscription(
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Subscription subscription = subscriptionService.getUserSubscription(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(subscription);
    }

    @PostMapping("/upgrade")
    public ResponseEntity<Subscription> upgradeSubscription(
            @RequestHeader(JWT_HEADER) String jwt,
            @RequestParam PlanType planType
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Subscription subscription = subscriptionService.upgradeSubscription(user.getId(), planType);
        return ResponseEntity.status(HttpStatus.OK).body(subscription);
    }
}
