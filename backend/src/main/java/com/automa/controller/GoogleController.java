package com.automa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.MessageResponse;
import com.automa.services.interfaces.IGoogle;
import com.automa.utils.ResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/google")
public class GoogleController {

    private final IGoogle googleService;

    public GoogleController(IGoogle googleService) {
        this.googleService = googleService;
    }

    @GetMapping("/callback")
    public void googleCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            if (error != null) {
                ResponseUtils.sendHtmlResponseWithPostMessageScript(response, error);
                return;
            } else if (code != null) {
                MessageResponse messageResponse = googleService.googleCallback(code, request);
                ResponseUtils.sendHtmlResponseWithPostMessageScript(response, messageResponse.getMessage());
                return;
            }

            ResponseUtils.sendJsonResponse(response, new MessageResponse("Invalid request"));

        } catch (Exception e) {
            ResponseUtils.sendHtmlResponseWithPostMessageScript(response, e.getMessage());
        }
    }

}
