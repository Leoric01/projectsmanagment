package com.leoric.services;

import com.leoric.models.PlanType;
import com.leoric.models.Subscription;
import com.leoric.models.User;
import com.leoric.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;


    @Override
    public Subscription createSubscription(User user) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setGetSubscriptionEnDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getUserSubscription(Long userId) {
        return null;
    }

    @Override
    public Subscription upgradeSubscription(Long userId, PlanType planType) {
        return null;
    }

    @Override
    public boolean isValid(Subscription subscription) {
        return false;
    }
}
