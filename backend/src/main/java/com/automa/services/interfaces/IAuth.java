package com.automa.services.interfaces;

import org.springframework.http.ResponseEntity;

import com.automa.dto.MessageResponse;
import com.automa.dto.auth.ForgotPasswordRequest;
import com.automa.dto.auth.ResetPasswordRequest;
import com.automa.dto.auth.SignInRequest;
import com.automa.dto.auth.SignInResponse;
import com.automa.dto.auth.SignUpRequest;

public interface IAuth {

    ResponseEntity<MessageResponse> signUp(SignUpRequest request);
    SignInResponse signIn(SignInRequest request);
    ResponseEntity<MessageResponse> forgotPassword(ForgotPasswordRequest request);
    ResponseEntity<MessageResponse> resetPassword(ResetPasswordRequest request);
}
