package com.automa.entity;

import com.automa.entity.action.Action;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Data
@Entity
@Table(name = "flows")
@AllArgsConstructor
@NoArgsConstructor
public class Flow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_action_id", nullable = false)
    @ToString.Exclude
    private Action fromAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_action_id", nullable = false)
    @ToString.Exclude
    private Action toAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    @ToString.Exclude
    private Workflow workflow;
}
