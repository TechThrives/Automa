package com.automa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.automa.entity.flow.Flow;

@Repository
public interface FlowRepository extends JpaRepository<Flow, UUID> {
}
