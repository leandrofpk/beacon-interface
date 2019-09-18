package com.example.beacon.vdf.infra;

import com.example.beacon.interfac.api.dto.PulseDto;
import com.example.beacon.shared.ByteSerializationFields;
import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.ICipherSuite;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoadCertificateFromUriServiceTest {

    @Test
    public void loadCert() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        URL CERT_URL = new URL("https://beacon.nist.gov//beacon/2.0/certificate/02288edbdc04bdeeb1c8140a41a1" +
                "ad40ab2ea7ad27fdd316946f3ec5aff1a7129f1fb5078202d75d42c201878b06d79c45bf37adb55f83aa213200834792b1da");
        X509Certificate x509Certificate = LoadCertificateFromUriService.loadCert(CERT_URL);
        PublicKey publicKey = x509Certificate.getPublicKey();
        assertEquals("X.509", publicKey.getFormat());
    }

    @Test
    public void validateSignaturePulseNist() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        final PublicKey publicKey = getCertificate();
        final PulseDto pulse = getPulse();

        ByteArrayOutputStream baos = new ByteSerializationFields(pulse).getBaos();

        String sign = pulse.getSignatureValue();
        final ICipherSuite cipherSuite = CipherSuiteBuilder.build(0);
        boolean verify = cipherSuite.verifyPkcs15(publicKey, sign, baos.toByteArray());

        assertTrue(verify);
    }

    private static PublicKey getCertificate() throws Exception {
        String url = "https://beacon.nist.gov//beacon/2.0/certificate/02288edbdc04bdeeb1c8140a41a1" +
        "ad40ab2ea7ad27fdd316946f3ec5aff1a7129f1fb5078202d75d42c201878b06d79c45bf37adb55f83aa213200834792b1da";

        URL CERT_URL = new URL(url);

        X509Certificate x509Certificate = LoadCertificateFromUriService.loadCert(CERT_URL);
        PublicKey publicKey = x509Certificate.getPublicKey();
        return publicKey;
    }

    private static PulseDto getPulse(){
        RestTemplate restTemplate = new RestTemplate();
        PulseDto pulse = restTemplate.getForObject("https://beacon.nist.gov/beacon/2.0/chain/1/pulse/548829", PulseDto.class);
        return pulse;
    }

}