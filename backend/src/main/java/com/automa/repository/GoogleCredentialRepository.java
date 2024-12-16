package com.automa.repository;

import com.automa.entity.credential.GoogleCredential;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import com.automa.entity.ApplicationUser;

@Repository
public interface GoogleCredentialRepository extends JpaRepository<GoogleCredential, UUID> {
    GoogleCredential findByRefreshToken(String refreshToken);

    Optional<GoogleCredential> findByUser(ApplicationUser user);

    Boolean existsByGoogleEmail(String googleEmail);
}