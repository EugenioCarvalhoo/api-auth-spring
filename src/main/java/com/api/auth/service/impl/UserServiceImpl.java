package com.api.auth.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UserServiceImpl implements UserService,UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       UserModel user = userRepo.findByUserName(userName)
       .orElseThrow(() -> { 
           throw new UsernameNotFoundException("Dados informado inválidos"); });
    
        Collection<SimpleGrantedAuthority> authorities = user.getRoles()
        .stream().map(role -> {
            return new SimpleGrantedAuthority(role.getName());
        }).collect(Collectors.toList());
        
       return new User(user.getUserName(), user.getPassword(),authorities);
    }
    
}
