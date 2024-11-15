package com.automa.repository;

import com.automa.entity.ApplicationUser;
import com.automa.entity.credential.Credential;
import com.automa.entity.credential.CredentialType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, UUID> {
    List<Credential> findByUser(ApplicationUser user);
    Optional<Credential> findByUserAndCredentialType(ApplicationUser user, CredentialType CredentialType);
    boolean existsByUserAndCredentialType(ApplicationUser user, CredentialType credentialType);
}