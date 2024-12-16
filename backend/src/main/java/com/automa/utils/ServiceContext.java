package com.automa.utils;

import com.automa.entity.credential.GoogleCredential;

public class ServiceContext {
    private static final ThreadLocal<GoogleCredential> googleCredential = new ThreadLocal<>();
    private static final ThreadLocal<String> username = new ThreadLocal<>();


    public static GoogleCredential getGoogleCredential() {
        return googleCredential.get();
    }

    public static void setGoogleCredential(GoogleCredential googleCredential) {
        ServiceContext.googleCredential.set(googleCredential);
    }

    public static void setUsername(String username) {
        ServiceContext.username.set(username);
    }

    public static String getUsername() {
        return username.get();
    }

    public static void clearContext() {
        username.remove();
        googleCredential.remove();
    }
}
