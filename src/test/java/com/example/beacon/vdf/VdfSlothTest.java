package com.example.beacon.vdf;

import org.junit.Test;

import java.math.BigInteger;

public class VdfSlothTest {

    @Test
    public void mod_op() {
        VdfSloth vdfSloth = new VdfSloth();

//        new BigInteger(ByteUtils.toHexString(xorValue), 16);
        int i = 9;
        BigInteger x = new BigInteger("80");
        BigInteger y = vdfSloth.mod_op(x, i);


        boolean b = vdfSloth.mod_verif(x, y, i);
        System.out.println(b);




    }
}