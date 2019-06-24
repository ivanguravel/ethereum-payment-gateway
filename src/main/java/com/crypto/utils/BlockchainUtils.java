package com.crypto.utils;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

import static org.web3j.crypto.Keys.createEcKeyPair;

public class BlockchainUtils {

    private static final String HEX_PREFIX = "0x";

    public static Credentials buildCredentials(String privateKey, String publicKey) {
        return Credentials.create(new ECKeyPair(new BigInteger(privateKey), new BigInteger(publicKey)));
    }


    public static ECKeyPair createECKeyPair() {
        try {
            return createEcKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Error creating new key pair.", e);
        }
    }

    public static String hexPrefix(String address) {
        if (!address.startsWith(HEX_PREFIX)) {
            address = HEX_PREFIX + address;
        }

        return address;
    }

}
