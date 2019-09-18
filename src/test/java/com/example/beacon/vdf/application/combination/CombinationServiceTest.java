package com.example.beacon.vdf.application.combination;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

import static com.example.beacon.vdf.infra.util.DateUtil.getTimeStampFormated;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class CombinationServiceTest {

    @Autowired
    CombinationService combinationService;

    @Test
    public void precommitSeedAndOneMore() throws Exception {
//        VdfPulseDtoPost vdfPulseDto = new VdfPulseDtoPost();
//        vdfPulseDto.setCertificateId("02288edbdc04bdeeb1c8140a41a1ad40ab2ea7ad27fdd316946f3ec5aff1a7129f1fb5078202d75d42c201878b06d79c45bf37adb55f83aa213200834792b1da");
//        vdfPulseDto.setSeed("100EFBEB29F458E06BF267EBB5CFCE768F9980317555E8C716B7CBE6C2BC07BE4983121D8DC0384AD6EF6ED4FC7C8EFDCB90509649AF3126A621368FA9073D12");
//        vdfPulseDto.setCipherSuite(0);
//        vdfPulseDto.setOriginEnum(OriginEnum.NIST);
//
//        SeedPostDto seedPostDto = new SeedPostDto("100EFBEB29F458E06BF267EBB5CFCE768F9980317555E8C716B7CBE6C2BC07" +
//                "BE4983121D8DC0384AD6EF6ED4FC7C8EFDCB90509649AF3126A621368FA9073D12",
//                OriginEnum.NIST.toString());

        combinationService.run(getTimeStampFormated(ZonedDateTime.now()));
    }


    @Test
    public void testXor(){
        String v1 = "100EFBEB29F458E06BF267EBB5CFCE768F9980317555E8C716B7CBE6C2BC07BE4983121D8DC0384AD6EF6ED4FC7C8EFDCB90509649AF3126A621368FA9073D12";
        String v2 = "02288edbdc04bdeeb1c8140a41a1ad40ab2ea7ad27fdd316946f3ec5aff1a7129f1fb5078202d75d42c201878b06d79c45bf37adb55f83aa213200834792b1da";

        byte[] xor = ByteUtils.xor(v1.getBytes(StandardCharsets.UTF_8), v2.getBytes(StandardCharsets.UTF_8));
        String s = ByteUtils.toHexString(xor);
        System.out.println(s);
        BigInteger bigInteger = new BigInteger(s, 16);
        System.out.println(bigInteger);
    }




}