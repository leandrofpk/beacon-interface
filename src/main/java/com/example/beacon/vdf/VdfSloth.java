package com.example.beacon.vdf;

import java.math.BigInteger;

public class VdfSloth {

    private static final BigInteger BIG_INT_QUATRO = new BigInteger("4");
    private static final BigInteger BIG_INT_DOIS = new BigInteger("2");
    private static final BigInteger BIG_INT_UM = new BigInteger("1");
    private final BigInteger p = new BigInteger("73237431696005972674723595250817150843");

    public BigInteger mod_op(BigInteger x, int t) {
        for (int i = 0; i < t; i++) {
            x = mod_sqrt_op(x, p);
            System.out.println(x);
        }
        return x;
    }

    private BigInteger mod_sqrt_op(BigInteger x, BigInteger p) {
        if (quad_res(x, p)){
//            pass
            return new BigInteger("0");
        } else {
            BigInteger pmais1duv4 = p.add(BIG_INT_UM).divide( BIG_INT_QUATRO);
            BigInteger y = x.modPow(pmais1duv4,p);
            return y ;
        }
    }

    private boolean quad_res(BigInteger x, BigInteger p) {
        BigInteger powT = x.modPow(p.subtract(BIG_INT_UM).divide(BIG_INT_DOIS), p);
        return powT.equals(BIG_INT_UM);
    }

    public boolean mod_verif(BigInteger y, BigInteger x, int t){

        for (int i = 0; i < t; i++) {
            y = y.modPow(BIG_INT_DOIS, p);

            if (!quad_res(y, p)) {
                y = (y.negate()).mod(p);
            }
        }

        if (x.mod(p).equals(y) || (x.negate()).mod(p).equals(y)){
            return true;
        } else {
            return false;
        }
    }

}