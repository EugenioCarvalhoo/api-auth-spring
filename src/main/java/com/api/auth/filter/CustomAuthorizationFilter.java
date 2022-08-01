package com.api.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.api.auth.token.Token;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private Token token;

    public CustomAuthorizationFilter(Token token) {
        this.token = token;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/v1/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {

            try {
                token.getAutorization(request, response, filterChain);
            } catch (ServletException | IOException e) {
                throw e;
            }catch (Exception e) {
                throw new RuntimeException("Erro na autenticaão.");
            }            
        }
    }

}
