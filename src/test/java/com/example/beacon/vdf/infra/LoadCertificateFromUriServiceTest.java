package com.example.beacon.vdf.infra;

import com.example.beacon.interfac.api.dto.PulseDto;
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

        final ByteArrayOutputStream serialize = serialize(pulse);

        String sign = pulse.getSignatureValue();
        final ICipherSuite cipherSuite = CipherSuiteBuilder.build(0);
        boolean verify = cipherSuite.verify(publicKey, sign, serialize.toByteArray());
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

    private static ByteArrayOutputStream serializeDOIS(PulseDto pulse) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(8192); // should be enough
        int lengthUri = pulse.getUri().getBytes(UTF_8).length;
        baos.write(ByteBuffer.allocate(4).putInt(lengthUri).array());
        baos.write(pulse.getUri().getBytes(UTF_8));



        return baos;
    }


        private static ByteArrayOutputStream serialize(PulseDto pulse){
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(8192); // should be enough
        System.out.println(getTimeStampFormated(pulse.getTimeStamp()));

        try {

            System.out.println(baos.size());
            baos.write(byteSerializeString(pulse.getUri()));

            baos.write(byteSerializeString(pulse.getVersion()));
            baos.write(encode4(pulse.getCipherSuite()));
            baos.write(encode4(pulse.getPeriod()));
            baos.write(byteSerializeHash(pulse.getCertificateId()));
            baos.write(encode8(pulse.getChainIndex()));
            baos.write(encode8(pulse.getPulseIndex()));
            baos.write(byteSerializeString(getTimeStampFormated(pulse.getTimeStamp())));
            baos.write(byteSerializeHash(pulse.getLocalRandomValue()));
            baos.write(byteSerializeHash(pulse.getExternal().getSourceId()));
            baos.write(encode8(pulse.getExternal().getStatusCode()));
            baos.write(byteSerializeHash(pulse.getExternal().getValue()));
            baos.write(byteSerializeHash(pulse.getListValues().get(0).getValue()));
            baos.write(byteSerializeHash(pulse.getListValues().get(1).getValue()));
            baos.write(byteSerializeHash(pulse.getListValues().get(2).getValue()));
            baos.write(byteSerializeHash(pulse.getListValues().get(3).getValue()));
            baos.write(byteSerializeHash(pulse.getListValues().get(4).getValue()));
            baos.write(byteSerializeHash(pulse.getPrecommitmentValue()));
            baos.write(encode4(pulse.getStatusCode()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos;
    }

    private static String getTimeStampFormated(ZonedDateTime timeStamp){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        String format = timeStamp.withZoneSameInstant((ZoneOffset.UTC).normalized()).format(dateTimeFormatter);
        return format;
    }

}