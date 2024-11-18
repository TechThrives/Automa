package com.automa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.automa.entity.action.Action;
import com.automa.entity.trigger.Trigger;

@Data
@Entity
@Table(name = "workflow")
@AllArgsConstructor
@NoArgsConstructor
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY) // Many workflows can belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // One Trigger can be associated with one Workflow
    @JoinColumn(name = "trigger_id", nullable = false) // Foreign key for the trigger
    private Trigger trigger;

    @OneToMany(mappedBy = "workflow", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // One workflow can have many actions
    private List<Action> actions;

    private Boolean isActive = true;
}
