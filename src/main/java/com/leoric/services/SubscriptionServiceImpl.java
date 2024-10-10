package com.leoric.services;

import com.leoric.models.PlanType;
import com.leoric.models.Subscription;
import com.leoric.models.User;
import com.leoric.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
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
        subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getUserSubscription(Long userId) {
        Subscription subscription = subscriptionRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(userId, "Subscription not found"));
        if (!isValid(subscription)) {
            subscription.setPlanType(PlanType.FREE);
            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(1));
            subscription.setSubscriptionStartDate(LocalDate.now());
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription upgradeSubscription(Long userId, PlanType planType) {
        Subscription subscription = getUserSubscription(userId);
        subscription.setPlanType(planType);
        subscription.setSubscriptionStartDate(LocalDate.now());
        if (planType.equals(PlanType.ANNUALLY)) {
            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(12));
        } else {
            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(1));
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean isValid(Subscription subscription) {
        PlanType planType = subscription.getPlanType();
        LocalDate subscriptionStartDate = subscription.getSubscriptionStartDate();
        LocalDate subscriptionEndDate = subscription.getGetSubscriptionEndDate();
        if (planType == null || subscriptionStartDate == null || subscriptionEndDate == null || subscriptionStartDate.isAfter(LocalDate.now())) {
            return false;
        }
        if (planType.equals(PlanType.FREE)) {
            return true;
        }
        if (planType.equals(PlanType.MONTHLY) || planType.equals(PlanType.ANNUALLY)) {
            return !subscriptionEndDate.isBefore(LocalDate.now());
        }

        return false;
    }
}
