package com.automa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Entity
@Table(name = "triggers")
@AllArgsConstructor
@NoArgsConstructor
public class Trigger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String name;


    @OneToOne(mappedBy = "trigger", fetch = FetchType.LAZY)
    private Workflow workflow;
}
