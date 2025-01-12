package com.automa.dto.payment;

import java.time.LocalDateTime;
import java.util.UUID;

import com.automa.entity.payment.PaymentMethod;
import com.automa.entity.payment.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponse {
    private UUID id;
    private Integer creditsPurchased;
    private Integer grandTotal;
    private PaymentStatus status;
    private LocalDateTime date;
    private PaymentMethod method;
}
