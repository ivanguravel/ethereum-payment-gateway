package com.crypto.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Logger;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PasswordEncryption {

    private final String TRANSFORMATION = "AES";

    @Value("${security.password.encryption.key}")
    private String ecrKey;

    public String doEncrypt(String inputStr) throws Exception {
        try {

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            byte[] key = ecrKey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] inputBytes = inputStr.getBytes();
            byte[] outputBytes = cipher.doFinal(inputBytes);

            return Base64Utils.encodeToString(outputBytes);

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new Exception("Error encrypting/decrypting file", ex);
        }
    }


    public  String doDecrypt(String encrptedStr) {
        try {
            Cipher dcipher = Cipher.getInstance(TRANSFORMATION);
            dcipher = Cipher.getInstance("AES");
            byte[] key = ecrKey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            dcipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] dec = Base64Utils.decode(encrptedStr.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, "UTF8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
