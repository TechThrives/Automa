package com.automa.entity.action;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "action_position")
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "action_id", nullable = false)
    @ToString.Exclude
    private Action action;

    @Column(nullable = false)
    private Float x;

    @Column(nullable = false)
    private Float y;
}
