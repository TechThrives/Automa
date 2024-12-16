package com.automa.entity.credential;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

import com.automa.entity.ApplicationUser;

@Data
@Entity
@Table(name = "credentials")
@AllArgsConstructor
@NoArgsConstructor
public class GoogleCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private List<String> scopes;

    @Column(nullable = false)
    private Long expiresInSeconds;

    @Column(nullable = false, unique = true, updatable = false)
    private String googleEmail;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false)
    private String fullName;
}