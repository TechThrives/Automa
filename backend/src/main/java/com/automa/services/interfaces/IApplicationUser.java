package com.automa.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.automa.dto.applicationuser.ApplicationUserResponse;

public interface IApplicationUser {
    
    List<ApplicationUserResponse> getAll();
    ApplicationUserResponse findByUsername(String username);
    ApplicationUserResponse findById(UUID userId);

}
