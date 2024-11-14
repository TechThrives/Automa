package com.automa.repository;

import com.automa.entity.ApplicationUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {
    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByPhoneNumber(String phoneNumber);

    Optional<ApplicationUser> findByPasswordResetToken(String passwordResetToken);

}