package com.automa.entity.credential;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.automa.entity.ApplicationUser;

@Data
@Entity
@Table(name = "credentials")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CredentialType credentialType;
}