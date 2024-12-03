package com.automa.services.implementation;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.repository.ActionInfoRepository;
import com.automa.services.interfaces.IActionInfo;

@Service
@Validated
public class ActionInfoService implements IActionInfo {

    private final ActionInfoRepository actionInfoRepository;

    public ActionInfoService(ActionInfoRepository actionInfoRepository) {    
        this.actionInfoRepository = actionInfoRepository;
    }

}
