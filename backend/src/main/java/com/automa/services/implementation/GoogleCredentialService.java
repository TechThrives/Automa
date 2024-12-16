package com.automa.services.implementation;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.config.GoogleConfig;
import com.automa.dto.MessageResponse;
import com.automa.entity.ApplicationUser;
import com.automa.entity.credential.GoogleCredential;
import com.automa.repository.GoogleCredentialRepository;
import com.automa.services.interfaces.IGoogleCredential;
import com.automa.utils.ContextUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpResponseException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Validated
public class GoogleCredentialService implements IGoogleCredential {

    private final GoogleConfig googleConfig;
    private final GoogleCredentialRepository googleCredentialRepository;
    private final ApplicationUserService applicationUserService;

    public GoogleCredentialService(GoogleConfig googleConfig,
            ApplicationUserService applicationUserService,
            GoogleCredentialRepository googleCredentialRepository) {
        this.googleCredentialRepository = googleCredentialRepository;
        this.applicationUserService = applicationUserService;
        this.googleConfig = googleConfig;
    }

    @Override
    public MessageResponse googleCallback(String code) {
        try {
            ApplicationUser user = applicationUserService.findByEmail(ContextUtils.getUsername());
            GoogleCredential existingCredential = user.getGoogleCredential();

            if (existingCredential != null) {
                GoogleTokenResponse tokenResponse = googleConfig.exchangeCodeForToken(code);
                String email = tokenResponse.parseIdToken().getPayload().getEmail();

                if (!email.equalsIgnoreCase(existingCredential.getGoogleEmail())) {
                    return new MessageResponse("Please use " + existingCredential.getGoogleEmail() + " to login.");
                }

                if (tokenResponse.getRefreshToken() != null) {
                    existingCredential.setRefreshToken(tokenResponse.getRefreshToken());
                }

                existingCredential.setAccessToken(tokenResponse.getAccessToken());
                existingCredential.setScopes(Arrays.asList(tokenResponse.getScope().split(" ")));
                existingCredential.setExpiresInSeconds(tokenResponse.getExpiresInSeconds());
                existingCredential.setFullName(tokenResponse.parseIdToken().getPayload().get("name").toString());
                existingCredential
                        .setProfileImageUrl(tokenResponse.parseIdToken().getPayload().get("picture").toString());

                googleCredentialRepository.save(existingCredential);

                return new MessageResponse("success");

            } else {
                GoogleTokenResponse tokenResponse = googleConfig.exchangeCodeForToken(code);
                String email = tokenResponse.parseIdToken().getPayload().getEmail();
                String fullName = tokenResponse.parseIdToken().getPayload().get("name").toString();
                String picture = tokenResponse.parseIdToken().getPayload().get("picture").toString();

                if (email == null || fullName == null || picture == null) {
                    return new MessageResponse("Add userinfo scope to your Google API");
                }

                if (googleCredentialRepository.existsByGoogleEmail(email)) {
                    return new MessageResponse("The Google email is already associated with another Automa account.");
                }

                if (tokenResponse.getRefreshToken() == null) {
                    return new MessageResponse("Connection Error");
                }

                GoogleCredential newCredential = new GoogleCredential();
                newCredential.setUser(user);
                newCredential.setAccessToken(tokenResponse.getAccessToken());
                newCredential.setRefreshToken(tokenResponse.getRefreshToken());
                newCredential.setScopes(Arrays.asList(tokenResponse.getScope().split(" ")));
                newCredential.setExpiresInSeconds(tokenResponse.getExpiresInSeconds());
                newCredential.setGoogleEmail(email);
                newCredential.setFullName(fullName);
                newCredential.setProfileImageUrl(picture);

                googleCredentialRepository.save(newCredential);

                return new MessageResponse("success");
            }
        } catch (IOException e) {
            if (e instanceof HttpResponseException httpResponseException &&
                    httpResponseException.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                try {
                    String errorContent = httpResponseException.getContent();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Error errorResponse = objectMapper.readValue(errorContent, Error.class);
                    return new MessageResponse(errorResponse.getError());
                } catch (JsonProcessingException e2) {
                    System.out.println("Error parsing error response: " + e2.getMessage());
                }
            }
            return new MessageResponse("failed");
        }

    }

    @Override
    public GoogleCredential findById(UUID id) {
        return googleCredentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Credential not found"));
    }

    @Override
    public GoogleCredential update(String refreshToken) {
        try {
            GoogleCredential googleCredential = googleCredentialRepository.findByRefreshToken(refreshToken);
            GoogleTokenResponse tokenResponse = googleConfig.refreshToken(refreshToken);
            googleCredential.setAccessToken(tokenResponse.getAccessToken());
            return googleCredentialRepository.save(googleCredential);
        } catch (IOException e) {
            if (e instanceof HttpResponseException httpResponseException &&
                    httpResponseException.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                try {
                    String errorContent = httpResponseException.getContent();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Error errorResponse = objectMapper.readValue(errorContent, Error.class);
                    throw new RuntimeException("Error refreshing access token: " + errorResponse.getError());
                } catch (JsonProcessingException e2) {
                    System.out.println("Error parsing error response: " + e2.getMessage());
                }
            }
            throw new RuntimeException("Error refreshing access token");
        }
    }

    @Override
    public void deleteById(UUID id) {
        if (googleCredentialRepository.existsById(id)) {
            googleCredentialRepository.deleteById(id);
        } else {
            throw new RuntimeException("Credential not found with ID: " + id);
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
