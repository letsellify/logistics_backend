package com.letsellify.logistics.common.security.config.keys;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@ConfigurationProperties(prefix = "rsa.refresh-token")
public record RefreshTokenRsaKey(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
