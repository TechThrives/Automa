package com.automa.services.implementation;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.config.GoogleConfig;
import com.automa.dto.MessageResponse;
import com.automa.services.interfaces.IGoogle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpResponseException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Validated
public class GoogleService implements IGoogle {

    private final GoogleConfig googleConfig;

    public GoogleService(GoogleConfig googleConfig) {
        this.googleConfig = googleConfig;
    }

    @Override
    public MessageResponse googleCallback(String code, HttpServletRequest request) {
        try {

            GoogleTokenResponse tokenResponse = googleConfig.exchangeCodeForToken(code);

            //TODO: Store access token in the database
            return new MessageResponse("success");

        } catch (IOException e) {
            if (e instanceof HttpResponseException httpResponseException &&
                    httpResponseException.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                try {
                    String errorContent = httpResponseException.getContent();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Error errorResponse = objectMapper.readValue(errorContent, Error.class);
                    return new MessageResponse(errorResponse.getError());
                } catch (Exception e2) {
                    System.out.println("Error parsing error response: " + e2.getMessage());
                }
            }
            return new MessageResponse("failed");
        }
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Error {
    private String error;
    private String error_description;
}
