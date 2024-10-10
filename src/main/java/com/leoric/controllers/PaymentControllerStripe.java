package com.leoric.controllers;

import com.leoric.requests.ChargeRequest;
import com.leoric.response.PaymentResponse;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.leoric.config.JwtConstant.JWT_HEADER;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentControllerStripe {

    @PostMapping("/charge")
    public ResponseEntity<PaymentResponse> charge(
            @RequestHeader(JWT_HEADER) String jwt,
            @RequestBody ChargeRequest request
    ) {
        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", request.getAmount() * 100);
            chargeParams.put("currency", "CZK");
            chargeParams.put("source", request.getToken());
            Charge charge = Charge.create(chargeParams);

            PaymentResponse response = new PaymentResponse();
            response.setId(charge.getId());
            response.setAmount(charge.getAmount().intValue());
            response.setCurrency(charge.getCurrency());
            response.setStatus(charge.getStatus());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}