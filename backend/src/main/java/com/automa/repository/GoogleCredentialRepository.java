package com.automa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automa.entity.ApplicationUser;
import com.automa.entity.credential.Google;

@Repository
public interface GoogleCredentialRepository extends JpaRepository<Google, UUID> {
    Optional<Google> findByUser(ApplicationUser user);
}
