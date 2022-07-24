package com.api.auth.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.auth.model.RoleModel;
import com.api.auth.model.UserModel;
import com.api.auth.request.RoleToUserRequest;
import com.api.auth.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.auth.config.AlgorithmConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    UserService userService;
    
    @Autowired
    AlgorithmConfig customAlgorithm;

    @PostMapping("/user/save")
    public ResponseEntity<UserModel> saveUser(UserModel user) {
        return ResponseEntity.created(getURI("/v1/user/save"))
                .body(userService.saveUser(user));
    }

    @PostMapping("/v1/role/save")
    public ResponseEntity<RoleModel> saveRole(RoleModel role) {
        return ResponseEntity.created(getURI("/v1/role/save"))
                .body(userService.saveRole(role));
    }

    @PutMapping("/role/addtoUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserRequest roleToUser) {
        userService.addRoleToUser(roleToUser);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/role/addtoUser")
    public ResponseEntity<?> refreshToken(@RequestBody RoleToUserRequest roleToUser) {
        userService.addRoleToUser(roleToUser);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/token/refresh_token")
    public void refreshToken(
            HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String bearer = "Bearer ";
        if (authorizationHeader != null &&
                authorizationHeader.startsWith(bearer)) {
            try {

                String refreshToken = authorizationHeader.substring(bearer.length());
                JWTVerifier verifier = JWT.require(customAlgorithm.getAlgorithm()).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                UserModel user = userService.getUser(username);
                String accesToken = createJWT( request, user, 10);
                
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accesToken", accesToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }

    }

    private URI getURI(String path) {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).toString());
    }

    private String createJWT(HttpServletRequest request, UserModel user, Integer minutes) {
        return JWT.create()
        .withSubject(user.getUserName())
        .withClaim("roles", user.getRoles()
        .stream().map(RoleModel::getName).collect(Collectors.toList()))
        .withIssuer(request.getRequestURL().toString())
        .withExpiresAt(new Date(
            System.currentTimeMillis() + minutes * 60 * 1000
        ))
        .sign(customAlgorithm.getAlgorithm());
    }
}
