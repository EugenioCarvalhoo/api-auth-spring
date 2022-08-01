package com.api.auth.token.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.api.auth.config.SecretToken;
import com.api.auth.dto.RoleDTO;
import com.api.auth.dto.UserDTO;
import com.api.auth.exception.ResponseError;
import com.api.auth.model.TokenModel;
import com.api.auth.token.Token;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TokenImpl implements Token {

    private static final Integer accesTokenExpiresAtMinutes = 10;
    private static final Integer refreshTokenExpiresAtMinutes = 30;
    private static final String BEARER = "Bearer ";
    private static Algorithm ALGORITHM = Algorithm.HMAC256(SecretToken.SECRET.getBytes());

    @Override
    public TokenModel getToken(StringBuffer requestURL, User user) {
        String accesToken = cerateToken(requestURL, user, accesTokenExpiresAtMinutes);
        String refreshToken = cerateToken(requestURL, user, refreshTokenExpiresAtMinutes);
        return new TokenModel(
                accesToken,
                refreshToken);
    }

    public void getAutorization(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null &&
                authorizationHeader.startsWith(BEARER)) {

            try {
                String token = authorizationHeader.substring(BEARER.length());
                JWTVerifier verifier = JWT.require(ALGORITHM).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                SecurityContextHolder.getContext()
                        .setAuthentication(
                                getUserAuthentication(username, roles));

                filterChain.doFilter(request, response);

            } catch (Exception e) {
                responseError(response, e);
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response, Function<String, UserDTO> user)
            throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null &&
                authorizationHeader.startsWith(BEARER)) {
            try {

                String refreshToken = authorizationHeader.substring(BEARER.length());
                JWTVerifier verifier = JWT.require(ALGORITHM).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                String accesToken = cerateToken(request.getRequestURL(), user.apply(username),
                        accesTokenExpiresAtMinutes);

                TokenModel tokens = new TokenModel(accesToken, refreshToken);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                responseError(response, e);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    private void responseError(HttpServletResponse response, Exception e)
            throws StreamWriteException, DatabindException, IOException {

        response.setHeader("error", e.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        ResponseError error = new ResponseError("Token inválido.", HttpStatus.FORBIDDEN);
        new ObjectMapper().writeValue(response.getOutputStream(), error);

    }

    private UsernamePasswordAuthenticationToken getUserAuthentication(String username, String[] roles) {
        List<SimpleGrantedAuthority> authorization = Arrays.stream(roles)
                .map(el -> new SimpleGrantedAuthority(el))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, authorization);
        return authentication;
    }

    private String cerateToken(StringBuffer requestURL, User user, Integer minutes) {
        return preToken(requestURL, minutes)
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(ALGORITHM);
    }

    private String cerateToken(StringBuffer requestURL, UserDTO user, Integer minutes) {
        return preToken(requestURL, minutes)
                .withSubject(user.getUserName())
                .withClaim("roles", user.getRoles()
                        .stream().map(RoleDTO::getName).collect(Collectors.toList()))
                .sign(ALGORITHM);
    }

    private Builder preToken(StringBuffer requestURL, Integer minutes) {
        return JWT.create()
                .withIssuer(requestURL.toString())
                .withExpiresAt(new Date(
                        System.currentTimeMillis() + minutes * 60 * 1000));
    }

}
