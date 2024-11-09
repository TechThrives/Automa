package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.IPayment;

@RestController
@Validated
public class PaymentController {

    private final IPayment paymentService;

    public PaymentController(IPayment paymentService) {
        this.paymentService = paymentService;
    }
}
