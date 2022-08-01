package com.api.auth.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.auth.dto.RoleDTO;
import com.api.auth.dto.UserDTO;
import com.api.auth.request.RoleToUserRequest;
import com.api.auth.service.UserService;
import com.api.auth.token.Token;

@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    Token token;

    @PostMapping("/user/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO user) {
        return ResponseEntity.created(getURI("/v1/user/save"))
                .body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<RoleDTO> saveRole(@RequestBody RoleDTO role) {
        return ResponseEntity.created(getURI("/v1/role/save"))
                .body(userService.saveRole(role));
    }

    @PutMapping("/role/addtoUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserRequest roleToUser) {
        userService.addRoleToUser(roleToUser);
        return ResponseEntity.ok()
                .build();
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/token/refresh")
    public void refreshToken(
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        token.refreshToken(request, response, 
        userName -> userService.getUser(userName));

    }

    private URI getURI(String path) {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).toString());
    }

}
