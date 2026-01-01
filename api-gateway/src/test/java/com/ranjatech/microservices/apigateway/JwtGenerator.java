package com.ranjatech.microservices.apigateway;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JwtGenerator {

	public static void main(String[] args) throws Exception {

		Path keyPath = Paths.get("D:/microservices-project/private.pem");

		String privateKeyPem = Files.readString(keyPath)
		        .replace("-----BEGIN PRIVATE KEY-----", "")
		        .replace("-----END PRIVATE KEY-----", "")
		        .replaceAll("\\s", "");

		byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);

		JWTClaimsSet claims = new JWTClaimsSet.Builder().subject("ranjan").issuer("ranjatech-auth")
				.claim("roles", List.of("USER")).expirationTime(Date.from(Instant.now().plusSeconds(3600)))
				.issueTime(new Date()).build();

		SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);

		jwt.sign(new RSASSASigner(privateKey));

		System.out.println(jwt.serialize());
	}
}
