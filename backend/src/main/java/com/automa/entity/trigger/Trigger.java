package com.automa.entity.trigger;
import com.automa.entity.Workflow;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @ToString.Exclude
    private Workflow workflow;

    @Column(nullable = false)
    private TriggerType triggerType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private HashMap<String, Object> config;
}
