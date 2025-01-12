package com.automa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.payment.CheckoutRequest;
import com.automa.dto.payment.PaymentSession;
import com.automa.services.interfaces.IPayment;
import com.stripe.exception.StripeException;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/payment")
public class PaymentController {

    private final IPayment paymentService;

    public PaymentController(IPayment paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<PaymentSession> checkout(@Valid @RequestBody CheckoutRequest checkoutRequest) throws StripeException {
        return new ResponseEntity<>(paymentService.createSession(checkoutRequest), HttpStatus.OK);
    }

    @PostMapping("/process-credit-purchase")
    public void processCreditPurchase(String sessionId) throws StripeException {
        paymentService.processCreditPurchase(sessionId);
    }
}
