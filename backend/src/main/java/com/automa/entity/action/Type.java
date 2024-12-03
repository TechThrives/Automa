package com.automa.entity.action;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "action_type")
@AllArgsConstructor
@NoArgsConstructor
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private BaseType baseType;

    @Column(nullable = false)
    private ActionType actionType;
}
