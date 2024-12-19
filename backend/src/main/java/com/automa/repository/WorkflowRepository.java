package com.automa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automa.entity.Workflow;
import com.automa.entity.action.ActionType;

import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, UUID> {
    List<Workflow> findByUser_Email(String email);
    List<Workflow> findByTrigger_Type(ActionType actionType);
}
