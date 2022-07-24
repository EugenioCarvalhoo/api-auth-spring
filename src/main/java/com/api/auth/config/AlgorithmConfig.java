package com.api.auth.config;

import org.springframework.stereotype.Component;

import com.auth0.jwt.algorithms.Algorithm;

@Component
public class AlgorithmConfig {
    
    private String secretAlgorithm = "secret";

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretAlgorithm.getBytes());
    }
}
