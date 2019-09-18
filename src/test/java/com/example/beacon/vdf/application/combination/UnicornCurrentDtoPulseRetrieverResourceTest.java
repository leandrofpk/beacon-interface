package com.example.beacon.vdf.application.combination;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.application.combination.dto.VdfPulseDtoPost;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.core.env.Environment;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;

import static com.example.beacon.shared.ByteSerializationFieldsUtil.*;
import static org.junit.Assert.assertTrue;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@TestPropertySource("classpath:application-test.properties")
public class UnicornCurrentDtoPulseRetrieverResourceTest {

//    @Autowired
    Environment env;

    VdfPulseDtoPost pulse;

//    @Before
    public void init(){
        pulse = new VdfPulseDtoPost();
        pulse.setCertificateId("04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227" +
                "fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132");
        pulse.setCipherSuite(0);
        pulse.setOriginEnum(OriginEnum.INMETRO);
        pulse.setSeed("100EFBEB29F458E06BF267EBB5CFCE768F9980317555E8C716B7CBE6C2BC07BE4983121D8DC0384AD6EF6ED4FC7C8EFDCB90509649AF3126A621368FA9073D12");
    }

//    @Test
    public void generateEntityPostToTest() throws Exception {
        final ICipherSuite cipherSuite = CipherSuiteBuilder.build(0);
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));

        byte[] bytes = VdfSerialize.serializeVdfDto(pulse);
        String sign = cipherSuite.signPkcs15(privateKey, bytes);

        pulse.setSignatureValue(sign);
        System.out.println(pulse);

        PublicKey publicKey = CriptoUtilService.loadPublicKeyFromCertificate(env.getProperty("beacon.x509.certificate"));
        boolean verify = cipherSuite.verifyPkcs15(publicKey, sign, VdfSerialize.serializeVdfDto(pulse));

        assertTrue(verify);
    }


    //    http://www.java2s.com/Tutorial/Java/0490__Security/BasicclassforexploringPKCS1V15Signatures.htm
//    @Test
    public void test() throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Sign
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        PublicKey publicKey = CriptoUtilService.loadPublicKeyFromCertificate(env.getProperty("beacon.x509.certificate"));

        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        signature.initSign(privateKey);

        byte[] message = serialize(pulse).toByteArray();
//        byte[] message = "100EFBEB29F458E06BF267EBB5CFCE768F9980317555E8C716B7CBE6C2BC07BE4983121D8DC0384AD6EF6ED4FC7C8EFDCB90509649AF3126A621368FA9073D12".getBytes();

        signature.update(message);

        byte[] sigBytes = signature.sign();  // sign private key
        String s = Hex.toHexString(sigBytes);
        System.out.println(s);

        // Verify
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] decSig = cipher.doFinal(sigBytes);
        ASN1InputStream aIn = new ASN1InputStream(decSig);
        ASN1Sequence seq = (ASN1Sequence) aIn.readObject();

        System.out.println(ASN1Dump.dumpAsString(seq));

        MessageDigest hash = MessageDigest.getInstance("SHA-256", "BC");
        hash.update(message);

        ASN1OctetString sigHash = (ASN1OctetString) seq.getObjectAt(1);
        System.out.println(MessageDigest.isEqual(hash.digest(), sigHash.getOctets()));
    }

    private ByteArrayOutputStream serialize(VdfPulseDtoPost pulse) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048); // should be enough
//        baos.write(byteSerializeHash(pulse.getCertificateId()));
//        baos.write(encode4(pulse.getCipherSuite()));
//        baos.write(byteSerializeString(pulse.getOriginEnum().toString()));
//        baos.write(byteSerializeHash(pulse.getSeed()));
        return baos;
    }

}