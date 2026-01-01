package com.ranjatech.microservices.auth_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.ranjatech.microservices.auth_service.entity.Role;
import com.ranjatech.microservices.auth_service.entity.User;

@Service
public class JwtService {

	private final JwtEncoder jwtEncoder;

	public JwtService(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	public String generateToken(User user) {

		Instant now = Instant.now();

		String scope = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));

		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("auth-service").issuedAt(now)
				.expiresAt(now.plus(1, ChronoUnit.HOURS)).subject(user.getUsername()).claim("roles", scope).build();

		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}
