package com.automa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.dto.applicationuser.ApplicationUserResponse;
import com.automa.services.interfaces.IApplicationUser;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@Validated
@RequestMapping("/api/user")
public class ApplicationUserController {

    private final IApplicationUser applicationUserService;

    public ApplicationUserController(IApplicationUser applicationUserService) { 
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ApplicationUserResponse>> getUsers(HttpServletRequest request) {
        return new ResponseEntity<>(applicationUserService.getAll(), HttpStatus.OK);
    }

}
