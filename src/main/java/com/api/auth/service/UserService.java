package com.api.auth.service;

import java.util.List;

import com.api.auth.dto.UserDTO;
import com.api.auth.model.RoleModel;
import com.api.auth.model.UserModel;
import com.api.auth.request.RoleToUserRequest;


public interface UserService {

    UserModel saveUser(UserDTO user);
    
    RoleModel saveRole(RoleModel role);
    
    UserModel getUser(String userName);
    
    List<UserModel> getUsers();

    void addRoleToUser(RoleToUserRequest roleToUser);
}
