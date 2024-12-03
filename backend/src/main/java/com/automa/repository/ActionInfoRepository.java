package com.automa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automa.entity.action.ActionInfo;

@Repository
public interface ActionInfoRepository extends JpaRepository<ActionInfo, UUID> {

}
