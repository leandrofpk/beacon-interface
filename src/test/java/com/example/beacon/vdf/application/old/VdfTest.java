package com.example.beacon.vdf.application.old;

import com.example.beacon.vdf.application.old.Vdf;
import com.example.beacon.vdf.application.old.VdfException;
import com.example.beacon.vdf.application.old.VdfSeed;
import com.example.beacon.vdf.infra.util.DateUtil;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

public class VdfTest {

    private VdfSeed seed1;

    private VdfSeed seed2;

    @Before
    public void setup(){
        ZonedDateTime t1 = DateUtil.getTimestampOfNextRun(ZonedDateTime.parse("2019-08-23T13:31:00.000Z"));
        seed1 = new VdfSeed(t1, "E90B7075493F84181B7220BA2907363ABDA076BFE9AFD3FBCA19B3259350213F038814BE1575CD83F7EBEC6C5FDD0347B33236390E991A6673056217AD26DD91", "https://beacon.nist.gov/beacon/2.0/chain/1/pulse/524971");

        ZonedDateTime t2 = DateUtil.getTimestampOfNextRun(ZonedDateTime.parse("2019-08-23T13:32:00.000Z"));
        seed2 = new VdfSeed(t2, "0281181ce3342936a94316846851ef420023744aaa687aa9594e48fc52869e865aed29cef426db27ae8e62d56ec32f0beb19fc6ca36d98b96198b75f6c5e671b", "https://random.uchile.cl/beacon/2.0/chain/4/pulse/538310");
    }


    @Test(expected = VdfException.class)
    public void shouldThrowExceptionWhenTryingToAddSeedWithStatusOtherThanOpen() {
        ZonedDateTime start = DateUtil.getTimestampOfNextRun(ZonedDateTime.parse("2019-08-23T13:04:00.000Z"));

        Vdf vdf = new Vdf();
        vdf.addSeed(seed1);
    }

    @Test
    public void teste(){
        Vdf vdf = new Vdf();
        vdf.startSubmissions(15);

        vdf.addSeed(seed1);
        System.out.println(vdf.getCurrentHash());

        vdf.addSeed(seed2);
        System.out.println(vdf.getCurrentHash());

        vdf.startProcessing();
    }

    @Test
    public void test(){
        String currentHash = "04B73AFE1FA62CF42BF79FC9153329C671B983FC8685BC1946AF576D20BA08A65A402991EDC372FABC46AAB58FD41493022800EEA7107BC381B2A7D23D7030C3";
        String honest = "1F95EA3C9358DE6167951A6495C25222C74B8C557B1880201E5C216C1981D16148A3A233789AF1A054182214A123C3B00C2AB293780ED094EE105ED85C3B122A";

        byte[] xor = ByteUtils.xor(currentHash.getBytes(StandardCharsets.UTF_8), honest.getBytes(StandardCharsets.UTF_8));
//        String s = ByteUtils.toBinaryString(xor);

//        System.out.println(s);
//
//
//        BigDecimal bigDecimal = new BigDecimal(honest);
//        System.out.println(bigDecimal);

        BigInteger bigInt = new BigInteger(ByteUtils.toHexString(xor), 16);
        System.out.println(bigInt);



    }


}