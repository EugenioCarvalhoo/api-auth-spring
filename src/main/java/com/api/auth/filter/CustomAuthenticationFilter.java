package com.api.auth.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String secretAlgorithm = "secret";
    private Integer accesTokenExpiresAtMinutes = 10;
    private Integer refreshTokenExpiresAtMinutes = 30;
    
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
       
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        return authenticationManager
        .authenticate( new UsernamePasswordAuthenticationToken(userName, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String accesToken = createJWT(request, user, accesTokenExpiresAtMinutes);
        String refreshToken = createJWT(request, user, refreshTokenExpiresAtMinutes);
        
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accesToken", accesToken);
        tokens.put("refreshToken", refreshToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    private String createJWT(HttpServletRequest request, User user, Integer minutes) {
        Algorithm algorithm = Algorithm.HMAC256(secretAlgorithm.getBytes());
        return JWT.create()
        .withSubject(user.getUsername())
        .withClaim("roles", user.getAuthorities()
        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .withIssuer(request.getRequestURL().toString())
        .withExpiresAt(new Date(
            System.currentTimeMillis() + minutes * 60 * 1000
        ))
        .sign(algorithm);
    }
}
