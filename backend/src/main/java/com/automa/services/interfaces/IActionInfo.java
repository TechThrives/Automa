package com.automa.services.interfaces;

import java.util.List;
import java.util.Map;

import com.automa.entity.action.ActionGroup;
import com.automa.entity.action.ActionInfo;
import com.automa.entity.action.ActionType;

public interface IActionInfo {

    List<ActionInfo> getAll();

    List<ActionInfo> getTriggers();

    List<ActionInfo> getActions();

    ActionInfo getByActionType(ActionType actionType);

    List<ActionInfo> getByActionGroup(ActionGroup actionGroup);

    Map<ActionGroup, List<ActionInfo>> getGroupedActions();

    Map<ActionGroup, List<ActionInfo>> getGroupedTriggers();
}
