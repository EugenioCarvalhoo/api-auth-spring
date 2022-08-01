package com.api.auth.token;


import java.util.function.Function;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.User;

import com.api.auth.dto.UserDTO;
import com.api.auth.model.TokenModel;

public interface Token {

    TokenModel getToken(StringBuffer requestURL, User user);

   void getAutorization(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws Exception;

   void refreshToken(HttpServletRequest request, HttpServletResponse response, Function<String , UserDTO> userName) throws Exception;

}
