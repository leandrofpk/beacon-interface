package com.example.beacon.vdf;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class VdfTest {

    private final BigInteger p = new BigInteger("73237431696005972674723595250817150843");

//    @Test
    public void vdfTeste(){

        double x = 80;
//        x = x % p;
        int t = 9;

        LocalDateTime start = LocalDateTime.now();
        mod_op(x,t);
        LocalDateTime end = LocalDateTime.now();

        System.out.println(start);
        System.out.println(end);
    }

    private double mod_op(double x, int t) { // hash operation on an int with t iternations
//        x = x % p  Acho que o cara quis fazer um hash
        double x2 = 0;
        for (int i = 0; i < t; i++) {
            System.out.println("x:" + x);
            x2 = mod_sqrt_op(x, p);
            System.out.println("x2:" + x2 );
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
            BigDecimal bigDecimal = new BigDecimal(Math.pow(x, p.doubleValue()));

            double y = Math.pow(x, (p.doubleValue() + 1d) / 4d) % p.longValue();
//            double v = vPow % p.doubleValue();
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

//
//        System.out.println("quad_res:" + );

//        return false;
//        return Math.pow(x,(p.doubleValue()-1 / 2)) == 1;
    }

//    @Test
    public void testFormatDouble(){
        DecimalFormat df = new DecimalFormat(".00");
        double dblResult = Math.pow(4.2, 3);
        System.out.println(dblResult);
        System.out.println(df.format(dblResult));

        double i = 10;
        double j = 2;

        double v = i % j;

        System.out.println(v);

    }

//    @Test
    public void testBigInteger(){
        String s = "A72FAD21591F39EAF99DAA8E9D4CB162225271A048B511A5C0309AEF035DD8E1A8B4578546626D0E23AFC09015F72AE2F8276C723AC2A38E764DE8CF657339B4";
        String s2 = "1.174021671158539e+308";
        BigDecimal bigInteger = new BigDecimal(s2);

        System.out.println(bigInteger.toString());
    }

}