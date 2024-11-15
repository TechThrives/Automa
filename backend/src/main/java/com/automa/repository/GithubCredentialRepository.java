package com.automa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automa.entity.ApplicationUser;
import com.automa.entity.credential.Github;

@Repository
public interface GithubCredentialRepository extends JpaRepository<Github, UUID> {
    Optional<Github> findByUser(ApplicationUser user);
}
