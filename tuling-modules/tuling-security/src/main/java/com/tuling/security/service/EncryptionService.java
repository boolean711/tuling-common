package com.tuling.security.service;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.tuling.security.constants.RedisKeyPrefixConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EncryptionService {

    @Autowired
    @Qualifier("jacksonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;


    public String generateAndStoreKeyPair(String sessionId) {
        RSA rsa = new RSA();

        // 获取公私钥
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();

        // Store private key in Redis
        redisTemplate.opsForValue().set(String.format(RedisKeyPrefixConstants.PRIVATE_KEYB_ASE64, sessionId), privateKeyBase64);

        // Return public key as Base64 encoded string
        return publicKeyBase64;
    }

    public String decryptData(String sessionId, String encryptedData) {
        String format = String.format(RedisKeyPrefixConstants.PRIVATE_KEYB_ASE64, sessionId);

        String privateKeyBase64 = (String) redisTemplate.opsForValue().get(format);
        if (privateKeyBase64 == null) {
            throw new IllegalArgumentException("Private key not found in Redis for session: " + sessionId);
        }
        redisTemplate.delete(format);

        RSA rsa = new RSA(privateKeyBase64, null);

        // 使用私钥解密
        byte[] decryptedBytes = rsa.decrypt(Base64.getDecoder().decode(encryptedData), KeyType.PrivateKey);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
