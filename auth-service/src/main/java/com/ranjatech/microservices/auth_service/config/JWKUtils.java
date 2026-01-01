package com.ranjatech.microservices.auth_service.config;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class JWKUtils {

	public static RSAKey loadRSAKey(InputStream inputStream) throws Exception {
		String key = new String(inputStream.readAllBytes()).replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");

		byte[] decoded = Base64.getDecoder().decode(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
		PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

		return new RSAKey.Builder((java.security.interfaces.RSAPublicKey) null).privateKey(privateKey).build();
	}
}
