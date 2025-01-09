package com.automa.dto.applicationuser;

import java.util.UUID;

import lombok.Data;

@Data
public class ApplicationUserResponse {

    private UUID id;
    private String profileImageUrl;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private Integer credits;
    private Integer workflows;
    private Integer workflowRuns;
}
