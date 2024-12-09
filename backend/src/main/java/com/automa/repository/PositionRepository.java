package com.automa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automa.entity.action.Position;

public interface PositionRepository extends JpaRepository<Position, UUID> {

}
