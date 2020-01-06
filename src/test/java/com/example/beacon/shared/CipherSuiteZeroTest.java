package com.example.beacon.shared;

import com.example.beacon.vdf.infra.LoadCertificateFromUriService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.net.URL;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;

public class CipherSuiteZeroTest {

    @Test
    public void loadCert() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        URL CERT_URL = new URL("https://beacon.nist.gov//beacon/2.0/certificate/02288edbdc04bdeeb1c8140a41a1" +
                "ad40ab2ea7ad27fdd316946f3ec5aff1a7129f1fb5078202d75d42c201878b06d79c45bf37adb55f83aa213200834792b1da");
        X509Certificate x509Certificate = LoadCertificateFromUriService.loadCert(CERT_URL);
        PublicKey publicKey = x509Certificate.getPublicKey();
        assertEquals("X.509", publicKey.getFormat());
    }

}