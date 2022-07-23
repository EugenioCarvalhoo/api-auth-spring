package com.api.auth.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    UserService userService;

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

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    private URI getURI(String path) {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(path).toString());
    }
    
}
