package com.automa.services.implementation;

import java.util.List;

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

    public List<Action> runMethod() {

        // Action a = new Action();
        // a.setActionType(ActionType.SENDMAIL);
        // a.setName("null");
        // a.setDescription("null");

        // HashMap<String, Object> config = new HashMap<>();
        // config.put("sendTo", "null");
        // config.put("subject", "null");
        // config.put("body", "null");
        // a.setConfig(config);

        // actionRepository.save(a);

        return actionRepository.findAll();
    }

}
