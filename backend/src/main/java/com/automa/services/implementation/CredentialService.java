package com.automa.services.implementation;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.automa.dto.credential.GoogleCredentialDto;
import com.automa.entity.ApplicationUser;
import com.automa.entity.credential.Credential;
import com.automa.entity.credential.CredentialType;
import com.automa.entity.credential.Github;
import com.automa.entity.credential.Google;
import com.automa.repository.CredentialRepository;
import com.automa.repository.GithubCredentialRepository;
import com.automa.repository.GoogleCredentialRepository;
import com.automa.services.interfaces.ICredential;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CredentialService implements ICredential {

    private final CredentialRepository credentialRepository;
    private final GoogleCredentialRepository googleCredentialRepository;
    private final GithubCredentialRepository githubCredentialRepository;

    public CredentialService(CredentialRepository credentialRepository,
            GoogleCredentialRepository GoogleRepository, 
            GithubCredentialRepository GithubRepository) {
        this.credentialRepository = credentialRepository;
        this.googleCredentialRepository = GoogleRepository;
        this.githubCredentialRepository = GithubRepository;
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
            Object credentialDto) {
        Optional<Credential> optionalCredential = credentialRepository.findByUserAndCredentialType(user,
                credentialType);

        if (optionalCredential.isPresent()) {
            Credential existingCredential = optionalCredential.get();

            if (credentialType == CredentialType.GOOGLE) {
                
                Google existingGoogle = (Google) existingCredential;
                GoogleCredentialDto googleDetails = (GoogleCredentialDto) credentialDto;
                
                BeanUtils.copyProperties(googleDetails, existingGoogle);
                return googleCredentialRepository.save(existingGoogle);
                
            } else if (credentialType == CredentialType.GITHUB) {
                Github existingGithub = (Github) existingCredential;
                Github githubDetails = (Github) credentialDto;
                BeanUtils.copyProperties(githubDetails, existingGithub);
                return githubCredentialRepository.save(existingGithub);
            }
        } else {
            if (credentialType == CredentialType.GOOGLE) {
                System.out.println(credentialDto);

                Google google = new Google();
                GoogleCredentialDto googleDetails = (GoogleCredentialDto) credentialDto;

                google.setUser(user);
                google.setCredentialType(credentialType);

                BeanUtils.copyProperties(googleDetails, google);
                
                return googleCredentialRepository.save(google);
            } else if (credentialType == CredentialType.GITHUB) {
                Github Github = new Github();
                Github githubDetails = (Github) credentialDto;

                Github.setUser(user);
                BeanUtils.copyProperties(githubDetails, Github);
                return githubCredentialRepository.save(Github);
            }
        }

        return null;
    }
}