package com.automa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.GenericData;

import java.io.IOException;

@Configuration
public class GoogleConfig {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    // Method to exchange authorization code for an access token
    public GoogleTokenResponse exchangeCodeForToken(String code) throws IOException {
        return new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                new GsonFactory(),
                clientId,
                clientSecret,
                code,
                redirectUri).execute();
    }

    // Method to refresh an access token using the refresh token
    public GoogleTokenResponse refreshToken(String refreshToken) throws IOException {
        return new GoogleRefreshTokenRequest(
                new NetHttpTransport(),
                new GsonFactory(),
                refreshToken,
                clientId,
                clientSecret).execute();
    }

    public void revokeToken(String token) throws IOException {
        try {
            String revokeEndpoint = "https://oauth2.googleapis.com/revoke";

            GenericData data = new GenericData();
            data.put("token", token);

            HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();

            HttpRequest request = requestFactory.buildPostRequest(
                    new GenericUrl(revokeEndpoint),
                    new UrlEncodedContent(data));

            request.getHeaders().setContentType("application/x-www-form-urlencoded");

            request.execute();

        } catch (Exception e) {
            System.err.println("Error revoking token: " + e.getMessage());
        }
    }
}
