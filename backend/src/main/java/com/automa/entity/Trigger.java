package com.automa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.automa.entity.converter.JsonNodeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

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

    @Column(columnDefinition = "jsonb", nullable = false)
    @Convert(converter = JsonNodeConverter.class)  // Apply the converter here
    @JsonIgnoreProperties(ignoreUnknown = true)
    private JsonNode config;

    @OneToOne(mappedBy = "trigger", fetch = FetchType.LAZY)
    private Workflow workflow;
}
