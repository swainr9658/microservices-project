package com.ranjatech.microservices.auth_service.controller;

import com.ranjatech.microservices.auth_service.dto.LoginRequest;
import com.ranjatech.microservices.auth_service.dto.TokenResponse;
import com.ranjatech.microservices.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginRequest request) {
		String token = authService.login(request.getUsername(), request.getPassword());
		return new TokenResponse(token);
	}
}
