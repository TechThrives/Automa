package com.automa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automa.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}
