package com.automa.services.interfaces;

import java.util.List;

import com.automa.entity.action.ActionGroup;
import com.automa.entity.action.ActionInfo;
import com.automa.entity.action.ActionType;

public interface IActionInfo {

    public List<ActionInfo> getAll();
    public List<ActionInfo> getTriggers();
    public List<ActionInfo> getActions();
    public List<ActionInfo> getByActionType(ActionType actionType);
    public List<ActionInfo> getByActionGroup(ActionGroup actionGroup);
}
