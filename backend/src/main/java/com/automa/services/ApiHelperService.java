package com.automa.services;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class ApiHelperService {

    public RestClient buildClient() {
        return RestClient.builder()
                .defaultStatusHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        return response.getStatusCode().isError();
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {
                        String responseBody = "";
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                            responseBody = reader.lines().collect(Collectors.joining("\n"));
                        }

                        JsonObject errorJson = JsonParser.parseString(responseBody).getAsJsonObject();
                        String errorMessage = errorJson.getAsJsonObject("error").get("message").getAsString();
                        int errorCode = errorJson.getAsJsonObject("error").get("code").getAsInt();
                        String status = errorJson.getAsJsonObject("error").get("status").getAsString();

                        throw new RestClientResponseException(errorMessage, errorCode,
                                status, response.getHeaders(),
                                responseBody.getBytes(), null);
                    }
                })
                .build();
    }
}
