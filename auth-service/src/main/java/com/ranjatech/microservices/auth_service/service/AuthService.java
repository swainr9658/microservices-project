package com.ranjatech.microservices.auth_service.service;

import com.ranjatech.microservices.auth_service.entity.User;
import com.ranjatech.microservices.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public String login(String username, String password) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		return jwtService.generateToken(user);
	}
}
