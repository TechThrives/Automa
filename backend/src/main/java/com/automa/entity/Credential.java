package com.automa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@Data
@Entity
@Table(name = "credentials")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String credentialType;

    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode config;

    @ManyToOne(fetch = FetchType.LAZY)  // Many credentials can belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;
}
