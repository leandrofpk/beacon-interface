package com.example.beacon.shared;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class CipherSuiteZero implements ICipherSuite {

    public String getDigest(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] encodedhash = digest.digest(input.getBytes(UTF_8));
            return Hex.toHexString(encodedhash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getDigest(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] encodedhash = digest.digest(input);
            return Hex.toHexString(encodedhash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

//    http://www.java2s.com/Tutorial/Java/0490__Security/BasicclassforexploringPKCS1V15Signatures.htm
    @Override
    public String signPkcs15(PrivateKey privateKey, byte[] message) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Signature signature = Signature.getInstance("SHA512withRSA", "BC");
        signature.initSign(privateKey);
        signature.update(message);

        byte[] sigBytes = signature.sign();

        return Hex.toHexString(sigBytes);
    }

//    http://www.java2s.com/Tutorial/Java/0490__Security/BasicclassforexploringPKCS1V15Signatures.htm
    @Override
    public boolean verifyPkcs15(PublicKey publicKey, String sign, byte[] message) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

//        ByteUtils.fromHexString(hash)
        byte[] decSig = cipher.doFinal(ByteUtils.fromHexString(sign));
        ASN1InputStream aIn = new ASN1InputStream(decSig);
        ASN1Sequence seq = (ASN1Sequence) aIn.readObject();

        MessageDigest hash = MessageDigest.getInstance("SHA-512", "BC");
        hash.update(message);

        ASN1OctetString sigHash = (ASN1OctetString) seq.getObjectAt(1);
        return MessageDigest.isEqual(hash.digest(), sigHash.getOctets());
    }


//    public static boolean pkcs1Verify(PublicKey key, byte[] text,
//                                      byte[] signature) {
////        String algName = sha256 ? "SHA256withRSA" : "SHA1withRSA";
//        String algName = "SHA512withRSA";
////        String algName = "SHA1withRSAandMGF1";
//        try {
//            Signature s = Signature.getInstance(algName);
//            s.initVerify(key);
//            s.update(text);
//            return s.verify(signature);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }


}