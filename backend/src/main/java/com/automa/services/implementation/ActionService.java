package com.automa.services.implementation;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.entity.action.Action;
import com.automa.repository.ActionRepository;
import com.automa.services.interfaces.IAction;

@Service
@Validated
public class ActionService implements IAction {

    private final ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @Override
    public Action findById(UUID id) {
        return actionRepository.findById(id).orElseGet(() -> {
            Action action = new Action();
            action.setId(id);
            return action;
        });
    }

    public void delete(UUID id) {
        if (actionRepository.existsById(id)) {
            actionRepository.deleteById(id);
        }
    }

}
