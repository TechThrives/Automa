package com.automa.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "application_user")
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser {
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // One user can have many workflows
    private List<Workflow> workflows;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // One user can have many payments
    private List<Payment> payments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // One user can have many credentials
    private List<Credential> credentials;

    @Column(nullable = false)
    private Integer credits = 0;
}
