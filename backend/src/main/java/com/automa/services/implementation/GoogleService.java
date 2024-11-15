package com.automa.services.implementation;

import java.io.IOException;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.config.GoogleConfig;
import com.automa.dto.MessageResponse;
import com.automa.dto.credential.GoogleCredentialDto;
import com.automa.entity.credential.CredentialType;
import com.automa.repository.ApplicationUserRepository;
import com.automa.entity.ApplicationUser;
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
    private final ApplicationUserRepository applicationUserRepository;
    private final JwtService jwtService;
    private final CredentialService credentialService;

    public GoogleService(GoogleConfig googleConfig,
            ApplicationUserRepository applicationUserRepository,
            CredentialService credentialService,
            JwtService jwtService) {
        this.googleConfig = googleConfig;
        this.applicationUserRepository = applicationUserRepository;
        this.jwtService = jwtService;
        this.credentialService = credentialService;
    }

    @Override
    public MessageResponse googleCallback(String code, HttpServletRequest request) {
        try {

            String token = jwtService.extractJwtTokenFromCookies(request);

            if (token != null) {

                String username = jwtService.extractUsername(token);

                GoogleTokenResponse tokenResponse = googleConfig.exchangeCodeForToken(code);

                GoogleCredentialDto credentialDto = new GoogleCredentialDto();

                BeanUtils.copyProperties(tokenResponse, credentialDto);
                credentialDto.setExpiresIn(tokenResponse.getExpiresInSeconds());

                ApplicationUser user = applicationUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found!!!"));

                credentialService.createOrUpdateCredential(user, CredentialType.GOOGLE, credentialDto);
            }

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
