package com.automa.utils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResponseUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${frontend.uri}")
    private String frontendUriValue;

    private static String frontendUri;

    @PostConstruct
    public void init() {
        frontendUri = frontendUriValue;
    }

    public static void sendHtmlResponseWithPostMessageScript(HttpServletResponse response, String message,
            String targetOrigin) throws IOException {
        String jsonResponse = String.format("{ \"message\": \"%s\" }", StringUtils.escapeJavaScript(message));
        String script = String.format(
                "window.opener.postMessage(%s, '%s'); window.close();", jsonResponse, targetOrigin);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "<!DOCTYPE html><html><body><script>" + script + "</script></body></html>");
    }

    public static void sendHtmlResponseWithPostMessageScript(HttpServletResponse response, String message)
            throws IOException {
        String jsonResponse = String.format("{ \"message\": \"%s\" }", StringUtils.escapeJavaScript(message));
        System.out.println(frontendUri);
        String script = String.format(
                "window.opener.postMessage(%s, '%s'); window.close();", jsonResponse, frontendUri);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "<!DOCTYPE html><html><body><script>" + script + "</script></body></html>");
    }

    public static void sendJsonResponse(HttpServletResponse response, Object responseObject) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String jsonResponse = objectMapper.writeValueAsString(responseObject);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
