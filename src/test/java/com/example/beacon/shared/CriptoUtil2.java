package com.example.beacon.shared;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

public class CriptoUtil2 {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    public void test(){

    }


    /**
     * Signs the text according to the RSA PKCS15 algorithm.
     *
     * @param key
     *            the private key
     * @param text
     *            the text to be signed
     * @return the signed text, null on errors
     */
    public static byte[] pkcs1Sign(PrivateKey key, byte[] text) {
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.ENCRYPT_MODE, key);
            return c.doFinal(text);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verfies an RSA PKCS15 signature
     *
     * @param key
     *            the public key
     * @param text
     *            the text (supposedly) signed
     * @param signature
     *            the signature
//     * @param sha256
     *            whether SHA256 was used, if false SHA1 assumed
     * @return whether the signature is correct, null on errors
     */
    public static boolean pkcs1Verify(PublicKey key, byte[] text,
                                      byte[] signature) {
//        String algName = sha256 ? "SHA256withRSA" : "SHA1withRSA";
        String algName ="SHA512withRSA";
        try {
            Signature s = Signature.getInstance(algName);
            s.initVerify(key);
            s.update(text);
            return s.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
