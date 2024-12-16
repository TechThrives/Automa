package com.automa.services.interfaces;

import java.util.UUID;

import com.automa.dto.MessageResponse;
import com.automa.entity.credential.GoogleCredential;

public interface IGoogleCredential {

    GoogleCredential findById(UUID id);

    void deleteById(UUID id);

    MessageResponse googleCallback(String code);

    GoogleCredential update(String refreshToken);
}
