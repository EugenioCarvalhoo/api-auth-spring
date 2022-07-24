package com.api.auth.service.impl;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.auth.model.UserModel;
import com.api.auth.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

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
