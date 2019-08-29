package com.example.beaconinterface.vdf;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

public class VdfTest3Wladmir2 {

    private static final BigInteger BIG_INT_QUATRO = new BigInteger("4");
	private static final BigInteger BIG_INT_DOIS = new BigInteger("2");
	private static final BigInteger BIG_INT_UM = new BigInteger("1");
	private final BigInteger p = new BigInteger("73237431696005972674723595250817150843");

    public static void main(String[] args) {

        BigInteger x = new BigInteger("80");
//        x = x % p;  // não faz nada no python
//        int t = 99999;
        int t = 999;

        LocalDateTime startEval = LocalDateTime.now();
        VdfTest3Wladmir2 vdfTest3Wladmir = new VdfTest3Wladmir2();
        BigInteger y = vdfTest3Wladmir.mod_op(x, t);
        LocalDateTime endEval = LocalDateTime.now();

        long between = ChronoUnit.MILLIS.between(startEval, endEval);
        System.out.println("Eval - Elapsed in millis:" + between);

        LocalDateTime startVerify = LocalDateTime.now();
        boolean b = vdfTest3Wladmir.mod_verif(y, x, t);
        LocalDateTime endVerify = LocalDateTime.now();

        long between2 = ChronoUnit.MILLIS.between(startVerify, endVerify);
        System.out.println("Verify - Elapsed in millis:" + between2);
        System.out.println("verify:" + b);

        System.out.println("------------------------------------");
        String digest = vdfTest3Wladmir.getDigest(y.toString());
        System.out.println("Last number:" + y);
        System.out.println("Hash last number:" + digest);
    }

    private BigInteger mod_op(BigInteger x, int t) { // hash operation on an int with t iternations
//        x = x % p  Acho que o cara quis fazer um hash - não faz nada no python
        for (int i = 0; i < t; i++) {
            x = mod_sqrt_op(x, p);
        }
        return x;
    }

    private BigInteger mod_sqrt_op(BigInteger x, BigInteger p) {
        if (quad_res(x, p)){
//            pass
            return new BigInteger("0");
        } else {
//            y = pow(x, (p + 1) // 4, p)
            BigInteger pmais1duv4 = p.add(BIG_INT_UM).divide( BIG_INT_QUATRO);
            BigInteger y = x.modPow(pmais1duv4,p);
            return y ;
        }
    }

    private boolean quad_res(BigInteger x, BigInteger p) {
//        return pow(x, (p - 1) // 2, p) == 1
    	BigInteger powT = x.modPow(p.subtract(BIG_INT_UM).divide(BIG_INT_DOIS), p);
        return powT.equals(BIG_INT_UM);
    }

    private boolean mod_verif(BigInteger y, BigInteger x, int t){

        for (int i = 0; i < t; i++) {
//            y = pow(int(y), 2, p)
            y = y.modPow(BIG_INT_DOIS, p);

//            if not quad_res(y, p):
            if (!quad_res(y, p)) {
//                y = (-y) % p
                y = (y.negate()).mod(p);
            }
        }
//        end = datetime.datetime.now()
//            if x % p == y or (-x) % p == y:
        if (x.mod(p).equals(y) || (x.negate()).mod(p).equals(y)){
            return true;
        } else {
            return false;
        }
    }

    public String getDigest(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] encodedhash = digest.digest(input.getBytes(UTF_8));
            return bytesToHex(encodedhash).toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
