package com.ranjatech.microservices.auth_service.controller;

import com.ranjatech.microservices.auth_service.dto.LoginRequest;
import com.ranjatech.microservices.auth_service.dto.TokenResponse;
import com.ranjatech.microservices.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	AuthService authService;

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginRequest request) {
		return authService.login(request);
	}
}
