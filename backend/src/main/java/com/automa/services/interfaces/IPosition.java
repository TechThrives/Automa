package com.automa.services.interfaces;

import java.util.UUID;

import com.automa.entity.action.Position;

public interface IPosition {

    public Position findById(UUID id);
}
