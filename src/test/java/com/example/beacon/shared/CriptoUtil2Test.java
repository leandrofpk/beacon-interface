package com.example.beacon.shared;

import com.example.beacon.interfac.api.dto.PulseDto;
import com.example.beacon.vdf.infra.LoadCertificateFromUriService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;

public class CriptoUtil2Test {


    @Test
    public void validarPulsoNist() throws Exception {
        PublicKey publicKey = loadCertNist();
        PulseDto pulse = getPulse();

        //serializar
        ByteArrayOutputStream baos = new ByteSerializationFieldsUtil2(pulse).getBaos();
        // hash
        String digest = CipherSuiteBuilder.build(0).getDigest(baos.toByteArray());
        System.out.println(digest);

        //validar
        boolean b = CriptoUtil2.pkcs1Verify(publicKey, pulse.getSignatureValue().getBytes(StandardCharsets.UTF_8), digest.getBytes(StandardCharsets.UTF_8));
        System.out.println(b);
    }

    private PublicKey loadCertNist() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        URL CERT_URL = new URL("https://beacon.nist.gov//beacon/2.0/certificate/02288edbdc04bdeeb1c8140a41a1" +
                "ad40ab2ea7ad27fdd316946f3ec5aff1a7129f1fb5078202d75d42c201878b06d79c45bf37adb55f83aa213200834792b1da");
        X509Certificate x509Certificate = LoadCertificateFromUriService.loadCert(CERT_URL);
        PublicKey publicKey = x509Certificate.getPublicKey();
        return publicKey;
    }

    private static PulseDto getPulse(){
        RestTemplate restTemplate = new RestTemplate();
        PulseDto pulse = restTemplate.getForObject("https://beacon.nist.gov/beacon/2.0/chain/1/pulse/532453", PulseDto.class);
        return pulse;
    }

}