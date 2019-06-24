package com.crypto.utils;


import org.apache.commons.lang3.RandomStringUtils;

public class PasswordUtils {

    private static final String PWD_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZab" +
            "cdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\\\|;:\\'\\\",<.>/?";

    public static String generatePassword() {
        return RandomStringUtils.random(12, PWD_ALPHABET);
    }

    private PasswordUtils() {}
}
