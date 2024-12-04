package com.automa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automa.entity.action.Position;

@Repository
public interface ActionPositionRepository extends JpaRepository<Position, UUID> {

}
