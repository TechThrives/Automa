package com.automa.entity.action;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.automa.entity.Workflow;
import com.automa.entity.flow.Flow;

@Data
@Entity
@Table(name = "actions")
@AllArgsConstructor
@NoArgsConstructor
public class Action {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    @ToString.Exclude
    private Workflow workflow;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType type;

    @Column(nullable = false)
    private Float positionX;

    @Column(nullable = false)
    private Float positionY;

    @OneToOne(mappedBy = "trigger", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @ToString.Exclude
    private Workflow triggeredWorkflow;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Flow> outgoingFlows;

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Flow> incomingFlows;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private HashMap<String, Object> data;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<HashMap<String, Object>> output;
}
