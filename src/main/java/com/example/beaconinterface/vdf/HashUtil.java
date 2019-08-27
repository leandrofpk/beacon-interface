package com.example.beaconinterface.vdf;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HashUtil {

    public static String getDigest(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] encodedhash = digest.digest(input.getBytes(UTF_8));
            return bytesToHex(encodedhash).toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDigest(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] encodedhash = digest.digest(input);
            return bytesToHex(encodedhash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
