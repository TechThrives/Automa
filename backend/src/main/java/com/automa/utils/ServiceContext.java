package com.automa.utils;

public class ServiceContext {
    private static final ThreadLocal<String> googleAccessToken = new ThreadLocal<>();

    public static void setGoogleAccessToken(String accessToken) {
        ServiceContext.googleAccessToken.set(accessToken);
    }

    public static String getGoogleAccessToken() {
        return googleAccessToken.get();
    }

    public static void removeGoogleAccessToken() {
        googleAccessToken.remove();
    }
}
