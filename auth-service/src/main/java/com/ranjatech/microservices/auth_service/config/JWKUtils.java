package com.ranjatech.microservices.auth_service.config;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.nimbusds.jose.jwk.RSAKey;

public class JWKUtils {

	public static RSAKey loadKey(InputStream privateIs, InputStream publicIs) throws Exception {

		// PRIVATE KEY
		String privateKey = new String(privateIs.readAllBytes()).replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");

		byte[] privateBytes = Base64.getDecoder().decode(privateKey);
		PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateBytes);

		// PUBLIC KEY
		String publicKey = new String(publicIs.readAllBytes()).replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "").replaceAll("\\s+", "");

		byte[] publicBytes = Base64.getDecoder().decode(publicKey);
		X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicBytes);

		KeyFactory factory = KeyFactory.getInstance("RSA");

		RSAPrivateKey priv = (RSAPrivateKey) factory.generatePrivate(privateSpec);
		RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(publicSpec);

		return new RSAKey.Builder(pub).privateKey(priv).build();
	}
}
