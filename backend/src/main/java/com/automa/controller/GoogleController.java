package com.automa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.MessageResponse;
import com.automa.services.interfaces.IGoogle;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@Validated
@RequestMapping("/api/google")
public class GoogleController {

    private final IGoogle googleService;

    public GoogleController(IGoogle googleService) {
        this.googleService = googleService;
    }

    @GetMapping("/callback")
    public ResponseEntity<MessageResponse> googleCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            HttpServletRequest request) {
        try {
            if (error != null) {
                return new ResponseEntity<>(new MessageResponse(error), HttpStatus.BAD_REQUEST);
            } else if (code != null) {
                MessageResponse messageResponse = googleService.googleCallback(code, request);
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }

            return new ResponseEntity<>(new MessageResponse("Invalid Request"), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
