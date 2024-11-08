package com.automa.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    @Override
    public List<ApplicationUser> getAll() {
        return applicationUserRepository.findAll();
    }

}
