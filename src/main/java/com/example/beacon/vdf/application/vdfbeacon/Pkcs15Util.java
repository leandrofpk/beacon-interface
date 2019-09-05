package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.shared.CriptoUtilService;
import org.aspectj.bridge.IMessage;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;

public class Pkcs15Util {

//    public static String sign(PrivateKey privateKey, byte[] message) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
//        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
//        signature.initSign(privateKey);
//        signature.update(message);
//
//        byte[] sigBytes = signature.sign();
//
//        return Hex.toHexString(sigBytes);
//    }
//
//    public static boolean verify(PublicKey publicKey, String sign, byte[] message) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
//        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
//        cipher.init(Cipher.DECRYPT_MODE, publicKey);
//
//        byte[] decSig = cipher.doFinal(ByteUtils.fromHexString(sign));
//        ASN1InputStream aIn = new ASN1InputStream(decSig);
//        ASN1Sequence seq = (ASN1Sequence) aIn.readObject();
//
//        MessageDigest hash = MessageDigest.getInstance("SHA-256", "BC");
//        hash.update(message);
//
//        ASN1OctetString sigHash = (ASN1OctetString) seq.getObjectAt(1);
//        return MessageDigest.isEqual(hash.digest(), sigHash.getOctets());
//    }

}
