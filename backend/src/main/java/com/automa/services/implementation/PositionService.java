package com.automa.services.implementation;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.entity.action.Position;
import com.automa.repository.PositionRepository;
import com.automa.services.interfaces.IPosition;

@Service
@Validated
public class PositionService implements IPosition {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public Position findById(UUID id) {
        return positionRepository.findById(id).orElseGet(() -> new Position());
    }

    public Position findByActionId(UUID id) {
        Position position = positionRepository.findByActionId(id);
        if(position == null) {
            position = new Position();
        }
        return position;
    }
}
