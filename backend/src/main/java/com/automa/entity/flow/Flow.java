package com.automa.entity.flow;

import com.automa.entity.Workflow;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlowType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    @ToString.Exclude
    private Workflow workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_action_id", nullable = false)
    @ToString.Exclude
    private Action source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_action_id", nullable = false)
    @ToString.Exclude
    private Action target;
}
