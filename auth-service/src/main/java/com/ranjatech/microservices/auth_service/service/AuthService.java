package com.ranjatech.microservices.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ranjatech.microservices.auth_service.dto.LoginRequest;
import com.ranjatech.microservices.auth_service.dto.TokenResponse;
import com.ranjatech.microservices.auth_service.entity.User;
import com.ranjatech.microservices.auth_service.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtService jwtService;

	public TokenResponse login(LoginRequest request) {

		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		String token = jwtService.generateToken(user);

		return new TokenResponse(token);
	}
}
