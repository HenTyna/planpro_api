package com.planprostructure.planpro.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Component
public class PasswordUtils {
    private static final String CIPHER_INSTANCE_NAME = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static String encryptionKey;

    @Value("${password.encryption.key}")
    public void setEncryptionKey(String key) {
        encryptionKey = key;
    }

    public static String generateKey() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)
            );
            return new String(Hex.encode(hash));
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate encryption key", e);
        }
    }

    private static Cipher prepareAndInitCipher(int encryptionMode) throws Exception {
        if (encryptionKey == null || encryptionKey.isEmpty()) {
            throw new IllegalStateException("Encryption key not set");
        }

        byte[] secretKey = getKey(encryptionKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, SECRET_KEY_ALGORITHM);

        // Use first 16 bytes of hashed key as IV
        byte[] iv = new byte[16];
        System.arraycopy(secretKey, 0, iv, 0, 16);
        IvParameterSpec algorithmParameters = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
        cipher.init(encryptionMode, secretKeySpec, algorithmParameters);

        return cipher;
    }

    private static byte[] getKey(String originalKey) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(originalKey.getBytes(StandardCharsets.UTF_8));
    }

    public static String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            throw new IllegalArgumentException("Plain text cannot be null or empty");
        }

        try {
            byte[] dataBytes = plainText.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = prepareAndInitCipher(Cipher.ENCRYPT_MODE);
            byte[] encrypted = cipher.doFinal(dataBytes);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public static String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            throw new IllegalArgumentException("Encrypted text cannot be null or empty");
        }

        try {
            byte[] encrypted = Base64.getDecoder().decode(encryptedText);
            Cipher cipher = prepareAndInitCipher(Cipher.DECRYPT_MODE);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}