package com.ranjatech.microservices.auth_service.config;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class SecurityBeansConfig {

    @Value("${jwt.private-key-path}")
    private Resource privateKey;

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        try (InputStream is = privateKey.getInputStream()) {
            com.nimbusds.jose.jwk.RSAKey rsaKey = JWKUtils.loadRSAKey(is);
            JWKSource<SecurityContext> jwkSource =
                    new ImmutableJWKSet<>(new JWKSet(rsaKey));
            return new NimbusJwtEncoder(jwkSource);
        }
    }
}
