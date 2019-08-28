package com.example.beaconinterface.vdf;

import org.junit.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class VdfTest {

    private final BigInteger p = new BigInteger("73237431696005972674723595250817150843");

    @Test
    public void teste(){

        double x = 80;
//        x = x % p;
        int t = 999999;

        LocalDateTime start = LocalDateTime.now();
        mod_op(x,t);
        LocalDateTime end = LocalDateTime.now();

        System.out.println(start);
        System.out.println(end);

    }

    private void mod_op(double x, int t) { // hash operation on an int with t iternations
//        x = x % p  Acho que o cara quis fazer um hash

        for (int i = 0; i < t; i++) {
            mod_sqrt_op(x, p);
        }

    }

    private boolean mod_sqrt_op(double x, BigInteger p) {
//        if (quad_res(x, p)){
//            return pow(x, (p - 1) // 2, p) == 1
            return Math.pow(x, p.doubleValue()) == 1;
//        } else {
//
//        }
    }

}