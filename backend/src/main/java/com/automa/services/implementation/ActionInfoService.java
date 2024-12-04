package com.automa.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.entity.action.ActionGroup;
import com.automa.entity.action.ActionInfo;
import com.automa.entity.action.ActionType;
import com.automa.entity.action.BaseType;
import com.automa.repository.ActionInfoRepository;
import com.automa.services.interfaces.IActionInfo;

@Service
@Validated
public class ActionInfoService implements IActionInfo {

    private final ActionInfoRepository actionInfoRepository;

    public ActionInfoService(ActionInfoRepository actionInfoRepository) {
        this.actionInfoRepository = actionInfoRepository;
    }

    @Override
    public List<ActionInfo> getAll() {
        return actionInfoRepository.findAll();
    }

    @Override
    public List<ActionInfo> getTriggers() {
        return actionInfoRepository.findByType(BaseType.TRIGGER);
    }

    @Override
    public List<ActionInfo> getActions() {
        return actionInfoRepository.findByType(BaseType.ACTION);
    }

    @Override
    public List<ActionInfo> getByActionType(ActionType actionType) {
        return actionInfoRepository.findByActionType(actionType);
    }

    @Override
    public List<ActionInfo> getByActionGroup(ActionGroup actionGroup) {
        return actionInfoRepository.findByActionGroup(actionGroup);
    }

}
