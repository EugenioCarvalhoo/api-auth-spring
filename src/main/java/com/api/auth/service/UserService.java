package com.api.auth.service;

import java.util.List;

import com.api.auth.dto.RoleDTO;
import com.api.auth.dto.UserDTO;
import com.api.auth.request.RoleToUserRequest;


public interface UserService {

    UserDTO saveUser(UserDTO user);
    
    RoleDTO saveRole(RoleDTO role);
    
    UserDTO getUser(String userName);
    
    List<UserDTO> getUsers();

    void addRoleToUser(RoleToUserRequest roleToUser);
}
