package com.example.beacon.vdf.infra;

import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.x509.X509StreamParser;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.net.URL;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Scanner;

public class LoadCertificateFromUriService {

    public static X509Certificate loadCert(final URL url) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        try (Scanner scanner = new Scanner(url.openStream()).useDelimiter("\\Z");
             PemReader pemReader = new PemReader(new StringReader(scanner.next()))) {

            X509StreamParser parser = X509StreamParser.getInstance("Certificate", "BC");
            parser.init(new ByteArrayInputStream(pemReader.readPemObject().getContent()));

            return (X509Certificate) parser.read();
        }
    }



}
