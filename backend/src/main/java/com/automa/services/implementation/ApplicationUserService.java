package com.automa.services.implementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.dto.applicationuser.ApplicationUserResponse;
import com.automa.entity.ApplicationUser;
import com.automa.repository.ApplicationUserRepository;
import com.automa.services.interfaces.IApplicationUser;

@Service
@Validated
public class ApplicationUserService implements IApplicationUser {

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public List<ApplicationUserResponse> getAll() {
        List<ApplicationUser> users = applicationUserRepository.findAll();

        return users.stream()
                .map(user -> {
                    ApplicationUserResponse response = new ApplicationUserResponse();
                    BeanUtils.copyProperties(user, response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    public ApplicationUserResponse findByUsername(String username) {
        ApplicationUser user = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found!!!"));

            ApplicationUserResponse response = new ApplicationUserResponse();
            BeanUtils.copyProperties(user, response);
            return response;
    }

    public ApplicationUserResponse findById(UUID userId) {
        ApplicationUser user = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found!!!"));

            ApplicationUserResponse response = new ApplicationUserResponse();
            BeanUtils.copyProperties(user, response);
            return response;
    }

}
