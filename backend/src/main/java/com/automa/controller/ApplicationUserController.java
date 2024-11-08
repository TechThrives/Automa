package com.automa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automa.entity.ApplicationUser;
import com.automa.services.interfaces.IApplicationUser;

@RestController
@Validated
public class ApplicationUserController {

    private final IApplicationUser applicationUserService;

    public ApplicationUserController(IApplicationUser applicationUserService) { 
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<ApplicationUser>> getUsers() {

        List<ApplicationUser> users = applicationUserService.getAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
