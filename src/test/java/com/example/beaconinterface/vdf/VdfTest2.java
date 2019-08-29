package com.example.beaconinterface.vdf;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class VdfTest2 {

    private final BigInteger p = new BigInteger("73237431696005972674723595250817150843");

    public static void main(String[] args) {
        double x = 80;
//        x = x % p;  // não faz nada no python
        int t = 9;

        LocalDateTime start = LocalDateTime.now();
        new VdfTest2().mod_op(x,t);
        LocalDateTime end = LocalDateTime.now();

        System.out.println(start);
        System.out.println(end);
    }

    private double mod_op(double x, int t) { // hash operation on an int with t iternations
//        x = x % p  Acho que o cara quis fazer um hash - não faz nada no python
        double x2 = 0;
        for (int i = 0; i < t; i++) {
            x2 = mod_sqrt_op(x, p);
        }
        return x2;
    }

    private double mod_sqrt_op(double x, BigInteger p) {
        if (quad_res(x, p)){
//            pass
            return 0;
        } else {
//            y = pow(x, (p + 1) // 4, p)
            System.out.println("x: " + x);
            System.out.println("p:" + p.doubleValue());
//            BigDecimal bigDecimal = new BigDecimal(Math.pow(x, p.doubleValue()));
            double y = Math.pow(x, (p.doubleValue() + 1d) / 4d) % p.longValue(); // problema aqui...
            System.out.println("y:" + y);
            return y ;
        }
    }

    private boolean quad_res(double x, BigInteger p) {
//        return pow(x, (p - 1) // 2, p) == 1
        double powT = Math.pow(x, (p.doubleValue() - new Double(1) / new Double(2)));
        double v = p.doubleValue();
        double v1 = powT % v;

        return v1 == 1;
    }

}