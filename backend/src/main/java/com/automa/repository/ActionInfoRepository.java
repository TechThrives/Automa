package com.automa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automa.entity.action.ActionGroup;
import com.automa.entity.action.ActionInfo;
import com.automa.entity.action.ActionType;
import com.automa.entity.action.BaseType;

@Repository
public interface ActionInfoRepository extends JpaRepository<ActionInfo, UUID> {
    ActionInfo findByTypeAndActionTypeAndActionGroup(BaseType type, ActionType actionType, ActionGroup actionGroup);
    List<ActionInfo> findByType(BaseType type);
    ActionInfo findByActionType(ActionType actionType);
    List<ActionInfo> findByActionGroup(ActionGroup actionGroup);
}
