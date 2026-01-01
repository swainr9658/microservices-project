package com.ranjatech.microservices.auth_service.service;

import com.ranjatech.microservices.auth_service.entity.Role;
import com.ranjatech.microservices.auth_service.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

	@Value("${security.jwt.issuer}")
	private String issuer;

	@Value("${security.jwt.expiration}")
	private long expiration;

	@Value("${security.jwt.private-key}")
	private Resource privateKeyResource;

	private PrivateKey privateKey;

	@PostConstruct
	void init() throws Exception {
		String key = Files.readString(privateKeyResource.getFile().toPath()).replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");

		byte[] decoded = Base64.getDecoder().decode(key);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
		privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
	}

	public String generateToken(User user) {

		return Jwts.builder().setSubject(user.getUsername()).setIssuer(issuer)
				.claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
				.setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plusSeconds(expiration)))
				.signWith(privateKey, SignatureAlgorithm.RS256).compact();
	}
}
