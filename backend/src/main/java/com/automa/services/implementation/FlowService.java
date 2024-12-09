package com.automa.services.implementation;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.entity.flow.Flow;
import com.automa.repository.FlowRepository;
import com.automa.services.interfaces.IFlow;

@Service
@Validated
public class FlowService implements IFlow {

    private final FlowRepository flowRepository;

    public FlowService(FlowRepository flowRepository) {
        this.flowRepository = flowRepository;
    }

    @Override
    public Flow findById(UUID id) {
        return flowRepository.findById(id).orElseThrow(() -> new RuntimeException("Flow not found"));
    }

}
