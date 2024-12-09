package com.automa.services.interfaces;

import java.util.UUID;

import com.automa.entity.action.Action;

public interface IAction {
    public Action findById(UUID id);
}
