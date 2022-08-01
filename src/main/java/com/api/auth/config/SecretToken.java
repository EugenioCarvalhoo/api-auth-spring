package com.api.auth.config;

import org.springframework.beans.factory.annotation.Value;

public class SecretToken {
    
    public static String SECRET;

    @Value("token.secret")
    private String secret;

    @Value("token.secret")
    public static void setSECRET(String sECRET) {
        SECRET = sECRET;
    }
}
