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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    @ToString.Exclude
    private Workflow workflow;

    @OneToMany(mappedBy = "fromAction", cascade = CascadeType.ALL, fetch =
    FetchType.LAZY)
    @ToString.Exclude
    private List<Flow> outgoingFlows;

    @OneToMany(mappedBy = "toAction", cascade = CascadeType.ALL, fetch =
    FetchType.LAZY)
    @ToString.Exclude
    private List<Flow> incomingFlows;

    @Column(nullable = false)
    private ActionType actionType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private HashMap<String, Object> config;
}
