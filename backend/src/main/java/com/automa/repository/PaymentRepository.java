package com.automa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automa.entity.ApplicationUser;
import com.automa.entity.payment.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findBySessionId(String sessionId);

    List<Payment> findByUser(ApplicationUser user);
}
