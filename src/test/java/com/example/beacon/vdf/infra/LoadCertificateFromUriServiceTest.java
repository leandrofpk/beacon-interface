package com.example.beacon.vdf.infra;

import com.example.beacon.interfac.api.dto.PulseDto;
import com.example.beacon.shared.ByteSerializationFieldsUtil2;
import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.ICipherSuite;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.beacon.shared.ByteSerializationFieldsUtil.*;
import static java.nio.charset.StandardCharsets.UTF_8;
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
    public void validatePulse() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        final PublicKey publicKey = getCertificate();
        final PulseDto pulse = getPulse();

        ByteArrayOutputStream baos = new ByteSerializationFieldsUtil2(pulse).getBaos();

        String sign = pulse.getSignatureValue();
        final ICipherSuite cipherSuite = CipherSuiteBuilder.build(0);
        boolean verify = cipherSuite.verify(publicKey, sign, baos.toByteArray());
        System.out.println("Verify:" + verify);

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
        PulseDto pulse = restTemplate.getForObject("https://beacon.nist.gov/beacon/2.0/chain/1/pulse/532453", PulseDto.class);
        return pulse;
    }

//    private static ByteArrayOutputStream serialize(PulseDto pulse) throws IOException {
//        return new ByteSerializationFieldsUtil2(pulse).getBaos();
//    }

    private static String getTimeStampFormated(ZonedDateTime timeStamp){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        String format = timeStamp.withZoneSameInstant((ZoneOffset.UTC).normalized()).format(dateTimeFormatter);
        return format;
    }

}