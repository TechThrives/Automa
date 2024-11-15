package com.automa.dto.credential;

import lombok.Data;

@Data
public class GoogleCredentialDto {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}
