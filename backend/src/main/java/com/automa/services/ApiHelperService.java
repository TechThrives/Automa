package com.automa.services;

import java.io.*;
import java.net.*;

import org.springframework.stereotype.Service;

import com.google.gson.*;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;

@Service
public class ApiHelperService {


    @PostConstruct
    public void init() {
    }

    public JsonObject makeApiRequest(
            @NotNull String apiUrl, 
            @NotNull String accessToken, 
            @NotNull String requestMethod, 
            String jsonPayload) {

        try {
            URI uri = new URI(apiUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");

            if (jsonPayload != null) {
                connection.setDoOutput(true);
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(jsonPayload.getBytes());
                }
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();

                JsonObject errorJson = JsonParser.parseString(errorResponse.toString()).getAsJsonObject();
                JsonObject errorDetails = errorJson.getAsJsonObject("error");
                String errorMessage = errorDetails.get("message").getAsString();
                String errorCode = errorDetails.get("code").getAsString();
                String status = errorDetails.get("status").getAsString();

                throw new Exception("HTTP Error " + responseCode + ": " + errorMessage + " (Code: " + errorCode + ", Status: " + status + ")");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonResponse;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
