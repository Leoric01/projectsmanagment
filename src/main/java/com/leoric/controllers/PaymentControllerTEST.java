package com.leoric.controllers;

import com.leoric.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// TODO CHANGE IT
@RequestMapping("/api/payment/teeeeest")
@RequiredArgsConstructor
public class PaymentControllerTEST {

    private String apiKey;
    private String apiSecret;

    private final UserService userService;

//    @PostMapping("/{planType}")
//    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
//            @RequestHeader(JWT_HEADER) String jwt,
//            @PathVariable PlanType planType
//    ) throws Exception {
//        User user = userService.findUserProfileByJwt(jwt);
//        int amount = 220 * 100;
//        if (planType.equals(PlanType.ANNUALLY)){
//            amount = (int) Math.ceil(amount*0.8)*12;
//        }
//        try {
//            StripeClient stripeClient = new StripeClient(apiKey);
//            PaymentLinkCreateParams paymentLinkResponse = PaymentLinkCreateParams.builder().build();
//            paymentLinkResponse. .put("amount", amount);
//            paymentLinkResponse.put("currency", "CZK");
//            JSONObject customer = new JSONObject();
//            customer.put("name", user.getFullName());
//            customer.put("email", user.getEmail());
//
//            paymentLinkResponse.put("customer", customer);
//            JSONObject notify = new JSONObject();
//            notify.put("email", true);
//            paymentLinkResponse.put("notify", notify);
//            //TODO change port
//            paymentLinkResponse.put("callback_url", "http://localhost:5173/upgrade_plan/success?planType=" + planType);
//            PaymentLink paymentLink = stripeClient.paymentLinks().create(paymentLinkResponse);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
}