package com.api.auth.filter;

import org.springframework.stereotype.Component;

import com.auth0.jwt.algorithms.Algorithm;

@Component
public class CustomAlgorithm {
    
    private String secretAlgorithm = "secret";

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretAlgorithm.getBytes());
    }
}
