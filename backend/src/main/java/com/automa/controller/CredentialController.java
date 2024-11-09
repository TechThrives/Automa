package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.ICredential;

@RestController
@Validated
public class CredentialController {

    private final ICredential credentialService;

    public CredentialController(ICredential credentialService) {
        this.credentialService = credentialService;
    }
}
