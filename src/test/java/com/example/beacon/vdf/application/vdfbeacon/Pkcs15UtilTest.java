package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.shared.CriptoUtilService;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Pkcs15UtilTest {

    @Autowired
    Environment env;

//    private VdfPulseDtoPost getPulse(){
//        VdfPulseDtoPost pulse = new VdfPulseDtoPost();
//        pulse.setCertificateId("04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227" +
//                "fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132");
//        pulse.setCipherSuite(0);
//        pulse.setOriginEnum(OriginEnum.INMETRO);
//        pulse.setSeed("100EFBEB29F458E06BF267EBB5CFCE768F9980317555E8C716B7CBE6C2BC07BE4983121D8DC0384AD6EF6ED4FC7C8EFDCB90509649AF3126A621368FA9073D12");
//
//        return pulse;
//    }
//
//    @Test
//    public void signAndVerify() throws Exception {
//        VdfPulseDtoPost pulse = getPulse();
//
//        byte[] serialize = VdfPulseSerialize.serialize(pulse);
//        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
//
//        String sign = Pkcs15Util.sign(privateKey, serialize);
//
//        PublicKey publicKey = CriptoUtilService.loadPublicKeyFromCertificate(env.getProperty("beacon.x509.certificate"));
//        boolean verify = Pkcs15Util.verify(publicKey, sign, VdfPulseSerialize.serialize(getPulse()));
//
//        assertTrue(verify);
//    }

}