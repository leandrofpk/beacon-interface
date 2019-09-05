package com.example.beacon.shared;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface ICipherSuite {
    String getDigest(String input);
    String getDigest(byte[] input);
    String signBytes15(String plainTextBytes, PrivateKey privateKey) throws Exception;
    boolean verifySignBytes15(String plainText, String signature, PublicKey publicKey) throws Exception;
}
