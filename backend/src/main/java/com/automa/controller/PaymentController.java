package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.IPayment;

@RestController
@Validated
@RequestMapping("/api/payment")
public class PaymentController {

    private final IPayment paymentService;

    public PaymentController(IPayment paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/path")
    public String getMethodName() {
        return new String();
    }
}
