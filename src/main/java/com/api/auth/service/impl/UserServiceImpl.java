package com.api.auth.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.auth.exception.EntityException;
import com.api.auth.model.RoleModel;
import com.api.auth.model.UserModel;
import com.api.auth.repository.RoleRepository;
import com.api.auth.repository.UserRepository;
import com.api.auth.request.RoleToUserRequest;
import com.api.auth.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    RoleRepository roleRepo;

    @Override
    public UserModel saveUser(UserModel user) {
        return userRepo.save(user);
    }

    @Override
    public RoleModel saveRole(RoleModel role) {
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(RoleToUserRequest roleToUser) {
        UserModel user = userRepo.findByUserName(roleToUser.getUserName())
        .orElseThrow(() -> { throw new EntityException(); });

        RoleModel role =  roleRepo.findByName(roleToUser.getRoleName())
        .orElseThrow(() -> { throw new EntityException(); });
        
        user.getRoles().add(role);
    }

    @Override
    public UserModel getUser(String userName) {
        return userRepo.findByUserName(userName)
        .orElseThrow(() -> { throw new EntityException(); });
    }

    @Override
    public List<UserModel> getUsers() {
        return userRepo.findAll();
    }
    
}
