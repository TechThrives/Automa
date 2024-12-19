package com.automa.services.implementation.core;

import com.automa.entity.credential.GoogleCredential;
import com.automa.services.implementation.GoogleCredentialService;

import org.springframework.web.client.ResponseErrorHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automa.utils.ServiceContext;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class ApiHelperService {

    private final GoogleCredentialService googleCredentialService;

    private RestClient restClient;

    public ApiHelperService(GoogleCredentialService googleCredentialService) {
        this.googleCredentialService = googleCredentialService;
        restClient = buildClient();
    }

    private RestClient buildClient() {
        return RestClient.builder()
                .defaultStatusHandler(new CustomErrorHandler())
                .build();
    }

    public String executeWithAccessToken(String apiUrl, HttpMethod method, String body) {
        return executeWithTokenRefresh(apiUrl, method, body, 0);
    }

    private String executeWithTokenRefresh(String apiUrl, HttpMethod method, String body, int retryCount) {
        if (retryCount > 1) {
            throw new IllegalStateException("Max retry attempts exceeded for " + apiUrl);
        }

        try {
            return restClient
                    .method(method)
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + ServiceContext.getGoogleCredential().getAccessToken())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                    .retrieve()
                    .body(String.class);

        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {

                refreshAccessToken();
                return executeWithTokenRefresh(apiUrl, method, body, retryCount + 1);

            } else {
                throw e;
            }
        }
    }

    @Transactional
    private void refreshAccessToken() {
        System.out.println("Refreshing access token...");
        String refreshToken = ServiceContext.getGoogleCredential().getRefreshToken();
        GoogleCredential googleCredential = googleCredentialService.update(refreshToken);
        ServiceContext.setGoogleCredential(googleCredential);
    }

    private static class CustomErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().isError();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            String responseBody;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                responseBody = reader.lines().collect(Collectors.joining("\n"));
            }

            JsonObject errorJson = JsonParser.parseString(responseBody).getAsJsonObject();
            String errorMessage = errorJson.getAsJsonObject("error").get("message").getAsString();
            int errorCode = errorJson.getAsJsonObject("error").get("code").getAsInt();
            String status = errorJson.getAsJsonObject("error").get("status").getAsString();

            throw new RestClientResponseException(errorMessage, errorCode, status, response.getHeaders(),
                    responseBody.getBytes(), null);
        }
    }
}
