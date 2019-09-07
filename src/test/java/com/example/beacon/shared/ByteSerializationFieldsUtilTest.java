package com.example.beacon.shared;

import org.junit.Test;

import static org.junit.Assert.*;

public class ByteSerializationFieldsUtilTest {

    @Test
    public void encode4() {

        byte[] bytes = ByteSerializationFieldsUtil.encode4(4);
        System.out.println("t");

        byte[] bytes2 = ByteSerializationFieldsUtil.encode8(8);
        System.out.println("t");


    }
}