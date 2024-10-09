package com.leoric.services;

import com.leoric.models.PlanType;
import com.leoric.models.Subscription;
import com.leoric.models.User;
import org.springframework.stereotype.Service;

@Service
public interface SubscriptionService {
    Subscription createSubscription(User user);

    Subscription getUserSubscription(Long userId);

    Subscription upgradeSubscription(Long userId, PlanType planType);

    boolean isValid(Subscription subscription);
}
