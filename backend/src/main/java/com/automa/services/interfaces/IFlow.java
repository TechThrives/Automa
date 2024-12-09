package com.automa.services.interfaces;

import java.util.UUID;

import com.automa.entity.flow.Flow;

public interface IFlow {
    public Flow findById(UUID id);
}
