package com.automa.services.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.automa.entity.ApplicationUser;
import com.automa.entity.credential.Credential;
import com.automa.entity.credential.CredentialType;

public interface ICredential {

    Credential findCredentialById(UUID id);

    List<Credential> findCredentialsByUser(ApplicationUser user);

    Credential createOrUpdateCredential(ApplicationUser user,
            CredentialType credentialType,
            HashMap<String, Object> credentialDetails);

    void deleteCredential(UUID id);
}
