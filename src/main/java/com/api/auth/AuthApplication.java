package com.api.auth;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.auth.dto.UserDTO;
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
		return run -> {
			userService.saveRole(new RoleModel(null, "ROLE_USER"));
			userService.saveRole(new RoleModel(null, "ROLE_MANAGER"));
			userService.saveRole(new RoleModel(null, "ROLE_ADMIN"));

			UserDTO user1 =  new UserDTO();
			user1.setName("Carlos da Silva");
			user1.setUserName("carlos");
			user1.setPassword("123");
			userService.saveUser(user1);

			UserDTO user2 =  new UserDTO();
			user2.setName("Angela do Santos Souza");
			user2.setUserName("angela");
			user2.setPassword("123ad");
			userService.saveUser(user2);

			userService.addRoleToUser(
					new RoleToUserRequest("angela", "ROLE_USER"));

			userService.addRoleToUser(
					new RoleToUserRequest("carlos", "ROLE_ADMIN"));
		};
	}

}
