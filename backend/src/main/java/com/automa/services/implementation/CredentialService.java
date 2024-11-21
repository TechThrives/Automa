package com.automa.services.implementation;

import org.springframework.stereotype.Service;

import com.automa.entity.ApplicationUser;
import com.automa.entity.credential.Credential;
import com.automa.entity.credential.CredentialType;
import com.automa.repository.CredentialRepository;
import com.automa.services.interfaces.ICredential;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CredentialService implements ICredential {

    private final CredentialRepository credentialRepository;

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    public Credential findCredentialById(UUID id) {
        return credentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Credential not found"));
    }

    @Override
    public List<Credential> findCredentialsByUser(ApplicationUser user) {
        return credentialRepository.findByUser(user);
    }

    @Override
    public void deleteCredential(UUID id) {
        if (credentialRepository.existsById(id)) {
            credentialRepository.deleteById(id);
        } else {
            throw new RuntimeException("Credential not found with ID: " + id);
        }
    }

    @Override
    public Credential createOrUpdateCredential(ApplicationUser user, CredentialType credentialType,
            HashMap<String, Object> credentialDto) {

        if (credentialRepository.existsByConfigEmailAndCredentialTypeAndNotUser(credentialDto.get("email").toString(),
                credentialType.name(), user.getId())) {
            throw new RuntimeException("The email " + credentialDto.get("email") +
                    " is already connected to another account with the " + credentialType + " credential.");
        }

        Optional<Credential> optionalCredential = credentialRepository.findByUserAndCredentialType(user,
                credentialType);

        if (optionalCredential.isPresent()) {

            Credential existingCredential = optionalCredential.get();
            
            existingCredential.setConfig(credentialDto);

            return credentialRepository.save(existingCredential);

        } else {

            Credential newCredential = new Credential();
            newCredential.setUser(user);
            newCredential.setCredentialType(credentialType);
            newCredential.setConfig(credentialDto);

            return credentialRepository.save(newCredential);

        }
    }
}