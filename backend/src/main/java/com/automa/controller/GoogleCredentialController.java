package com.automa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.MessageResponse;
import com.automa.services.interfaces.IGoogleCredential;


@RestController
@Validated
@RequestMapping("/api/google")
public class GoogleCredentialController {

    private final IGoogleCredential googleCredentialService;

    public GoogleCredentialController(IGoogleCredential googleCredentialService) {
        this.googleCredentialService = googleCredentialService;
    }

    @GetMapping("/callback")
    public ResponseEntity<MessageResponse> googleCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error) {
        try {
            if (error != null) {
                return new ResponseEntity<>(new MessageResponse(error), HttpStatus.BAD_REQUEST);
            } else if (code != null) {
                MessageResponse messageResponse = googleCredentialService.googleCallback(code);
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }

            return new ResponseEntity<>(new MessageResponse("Invalid Request"), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
