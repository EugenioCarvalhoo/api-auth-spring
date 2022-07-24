package com.api.auth;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.auth.model.RoleModel;
import com.api.auth.model.UserModel;
import com.api.auth.request.RoleToUserRequest;
import com.api.auth.service.UserService;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return	run -> {
			userService.saveRole(new RoleModel(null, "ROLE_USER"));
			userService.saveRole(new RoleModel(null, "ROLE_MANAGER"));
			userService.saveRole(new RoleModel(null, "ROLE_ADMIN"));
			
			userService.saveUser(
				new UserModel(null, "Carlos da silva",
				 "Carlos", "carlos123", new ArrayList<>()));

			userService.addRoleToUser(
				new RoleToUserRequest("Carlos", "ROLE_USER"));
		};
	}

}
