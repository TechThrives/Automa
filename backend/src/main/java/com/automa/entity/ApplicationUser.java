package com.automa.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.automa.entity.credential.GoogleCredential;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "application_user")
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true, unique = true)
    private String passwordResetToken;

    @Column(nullable = true)
    private LocalDateTime passwordResetTokenExpiresAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // One user can have many workflows
    private List<Workflow> workflows;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // One user can have many payments
    private List<Payment> payments;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private GoogleCredential googleCredential;

    @Column(nullable = false)
    private Integer credits = 0;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
