package com.automa.entity.action;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.automa.entity.Flow;
import com.automa.entity.Workflow;

@Data
@Entity
@Table(name = "actions")
@AllArgsConstructor
@NoArgsConstructor
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    @ToString.Exclude
    private Workflow workflow;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BaseType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @OneToOne(mappedBy = "action", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Position position;

    @OneToMany(mappedBy = "fromAction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Flow> outgoingFlows;

    @OneToMany(mappedBy = "toAction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Flow> incomingFlows;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private HashMap<String, Object> data;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private HashMap<String, Object> output;
}
