package com.automa.services.implementation;

import org.springframework.stereotype.Service;

import com.automa.entity.ApplicationUser;
import com.automa.entity.credential.Credential;
import com.automa.entity.credential.CredentialType;
import com.automa.entity.credential.Github;
import com.automa.entity.credential.Google;
import com.automa.repository.CredentialRepository;
import com.automa.repository.GithubCredentialRepository;
import com.automa.repository.GoogleCredentialRepository;
import com.automa.services.interfaces.ICredential;

import java.util.HashMap;
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
            HashMap<String, Object> credentialDto) {

        String email = (String) credentialDto.get("email");

        if (credentialType == CredentialType.GOOGLE) {
            Optional<Google> existingGoogleCredential = googleCredentialRepository.findByEmail(email);

            if (existingGoogleCredential.isPresent()) {
                Google googleCredential = existingGoogleCredential.get();

                if (!googleCredential.getUser().equals(user)) {
                    throw new RuntimeException("This Google account is already connected to another user.");
                }
            }
        }

        Optional<Credential> optionalCredential = credentialRepository.findByUserAndCredentialType(user,
                credentialType);

        if (optionalCredential.isPresent()) {
            Credential existingCredential = optionalCredential.get();

            if (credentialType == CredentialType.GOOGLE) {

                Google existingGoogle = (Google) existingCredential;
                HashMap<String, Object> googleDetails = new HashMap<>(credentialDto);

                existingGoogle.setEmail((String) googleDetails.get("email"));
                existingGoogle.setAccessToken((String) googleDetails.get("accessToken"));
                existingGoogle.setRefreshToken((String) googleDetails.get("refreshToken"));
                existingGoogle.setExpiresInSeconds((Long) googleDetails.get("expiresInSeconds"));
                existingGoogle.setScope((String) googleDetails.get("scope"));

                return googleCredentialRepository.save(existingGoogle);

            } else if (credentialType == CredentialType.GITHUB) {
                Github existingGithub = (Github) existingCredential;

                HashMap<String, Object> githubDetails = new HashMap<>(credentialDto);

                existingGithub.setEmail((String) githubDetails.get("email"));
                existingGithub.setAccess((String) githubDetails.get("access"));
                existingGithub.setRefresh((String) githubDetails.get("refresh"));

                return githubCredentialRepository.save(existingGithub);
            }
        } else {
            if (credentialType == CredentialType.GOOGLE) {
                System.out.println(credentialDto);

                Google google = new Google();
                HashMap<String, Object> googleDetails = new HashMap<>(credentialDto);

                google.setUser(user);
                google.setCredentialType(credentialType);

                google.setEmail((String) googleDetails.get("email"));
                google.setAccessToken((String) googleDetails.get("accessToken"));
                google.setRefreshToken((String) googleDetails.get("refreshToken"));
                google.setExpiresInSeconds((Long) googleDetails.get("expiresInSeconds"));
                google.setScope((String) googleDetails.get("scope"));

                return googleCredentialRepository.save(google);
            } else if (credentialType == CredentialType.GITHUB) {
                Github Github = new Github();
                HashMap<String, Object> githubDetails = new HashMap<>(credentialDto);

                Github.setCredentialType(credentialType);
                Github.setUser(user);

                Github.setEmail((String) githubDetails.get("email"));
                Github.setAccess((String) githubDetails.get("access"));
                Github.setRefresh((String) githubDetails.get("refresh"));

                return githubCredentialRepository.save(Github);
            }
        }

        return null;
    }
}