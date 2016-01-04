package com.nju.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by xiaojuzhang on 2016/1/4.
 */
public class Crypt {

    private static final String AES = "AES";
    private static final int KEY_LENGTH = 256;


    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = KEY_LENGTH;

        SecureRandom secureRandom = new SecureRandom();

        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }
}
