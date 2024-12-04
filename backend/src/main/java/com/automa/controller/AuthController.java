package com.automa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.MessageResponse;
import com.automa.dto.auth.ForgotPasswordRequest;
import com.automa.dto.auth.ResetPasswordRequest;
import com.automa.dto.auth.SignInRequest;
import com.automa.dto.auth.SignInResponse;
import com.automa.dto.auth.SignUpRequest;
import com.automa.services.interfaces.IAuth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuth authService;

    public AuthController(IAuth authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<MessageResponse> signUp(
            @Valid @RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(
            @Valid @RequestBody SignInRequest request,
            HttpServletResponse response) {
        SignInResponse signInResponse = authService.signIn(request);
        return ResponseEntity.status(HttpStatus.OK).body(signInResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }
}