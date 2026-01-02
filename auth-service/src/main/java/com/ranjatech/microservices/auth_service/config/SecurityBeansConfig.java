package com.ranjatech.microservices.auth_service.config;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class SecurityBeansConfig {

	@Value("classpath:keys/private.pem")
	private Resource privateKey;

	@Value("classpath:keys/public.pem")
	private Resource publicKey;

	@Bean
	JwtEncoder jwtEncoder() throws Exception {

		RSAKey rsaKey = JWKUtils.loadKey(privateKey.getInputStream(), publicKey.getInputStream());

		JWKSource<SecurityContext> source = new ImmutableJWKSet<>(new JWKSet(rsaKey));

		return new NimbusJwtEncoder(source);
	}
}
