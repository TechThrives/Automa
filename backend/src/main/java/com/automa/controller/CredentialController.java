package com.automa.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.services.interfaces.ICredential;

@RestController
@Validated
@RequestMapping("/api/credential")
public class CredentialController {

    private final ICredential credentialService;

    public CredentialController(ICredential credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping("/path")
    public String getMethodName() {
        return new String();
    }
}
