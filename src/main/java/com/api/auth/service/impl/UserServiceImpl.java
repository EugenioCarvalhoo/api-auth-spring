package com.api.auth.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.auth.dto.RoleDTO;
import com.api.auth.dto.UserDTO;
import com.api.auth.exception.UserException;
import com.api.auth.model.RoleModel;
import com.api.auth.model.UserModel;
import com.api.auth.repository.RoleRepository;
import com.api.auth.repository.UserRepository;
import com.api.auth.request.RoleToUserRequest;
import com.api.auth.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepo;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDTO saveUser(UserDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserModel model = userRepo.save(user.parseModel());
        return new UserDTO(model);
    }

    @Override
    public RoleDTO saveRole(RoleDTO role) {
        RoleModel model = roleRepo.save(role.parseModel());
        return new RoleDTO(model);
    }

    @Override
    public void addRoleToUser(RoleToUserRequest roleToUser) {
        UserModel user = userRepo.findByUserName(roleToUser.getUserName())
        .orElseThrow(() -> { throw new UserException(); });

        RoleModel role =  roleRepo.findByName(roleToUser.getRoleName())
        .orElseThrow(() -> { throw new UserException(); });
        
        user.getRoles().add(role);
    }

    @Override
    public UserDTO getUser(String userName) {
        UserModel model = userRepo.findByUserName(userName)
        .orElseThrow(() -> { throw new UserException(); });
        return new UserDTO(model);
    }

    @Override
    public List<UserDTO> getUsers() {
        List<UserModel> modelList = userRepo.findAll();
        return modelList.stream().map(el -> new UserDTO(el))
        .collect(Collectors.toList());
    }
    
}
