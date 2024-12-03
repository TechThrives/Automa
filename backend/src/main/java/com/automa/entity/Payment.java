package com.automa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private Integer amount;

    private Integer credit;

    @ManyToOne(fetch = FetchType.LAZY)  // Many payments can belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;
}

enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    BANK_TRANSFER
}
