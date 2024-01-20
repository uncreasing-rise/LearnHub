package com.example.learnhub.security.utils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESUtils {


    @SneakyThrows
    public static String encrypt(String data, String key) throws Exception {
        // Generate a random initialization vector (IV)
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Generate a SecretKey from the given key using a key derivation function
        KeySpec keySpec = new PBEKeySpec(key.toCharArray(), iv, 65536, 256); // You can adjust these parameters
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] secretKeyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");

        // Initialize the Cipher for encryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        // Encrypt the data
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));

        // Combine the IV and encrypted data, then encode to Base64
        byte[] combined = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);
        String encryptedBase64 = Base64.getEncoder().encodeToString(combined);

        return encryptedBase64;
    }

    @SneakyThrows
    public static String decrypt(String encryptedData, String key) throws Exception {
        // Decode the Base64 string
        byte[] combined = Base64.getDecoder().decode(encryptedData);

        // Extract the IV and encrypted data
        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[combined.length - 16];
        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encryptedBytes, 0, encryptedBytes.length);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Generate a SecretKey from the given key using a key derivation function
        KeySpec keySpec = new PBEKeySpec(key.toCharArray(), iv, 65536, 256); // You can adjust these parameters
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] secretKeyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");

        // Initialize the Cipher for decryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        // Decrypt the data
        byte[] decryptedData = cipher.doFinal(encryptedBytes);

        return new String(decryptedData, "UTF-8");
    }
}
