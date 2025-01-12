package com.automa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.MessageResponse;
import com.automa.dto.payment.CheckoutRequest;
import com.automa.dto.payment.PaymentResponse;
import com.automa.dto.payment.PaymentSession;
import com.automa.services.interfaces.IPayment;
import com.stripe.exception.StripeException;

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
    public ResponseEntity<MessageResponse> processCreditPurchase(@RequestParam String sessionId) {
        try {
            return new ResponseEntity<>(paymentService.processCreditPurchase(sessionId), HttpStatus.OK);
        } catch (StripeException e) {
            return new ResponseEntity<>(new MessageResponse("Session Invalid"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getUserWorkflow() {
        return new ResponseEntity<>(paymentService.findByUser(), HttpStatus.OK);
    }
}
