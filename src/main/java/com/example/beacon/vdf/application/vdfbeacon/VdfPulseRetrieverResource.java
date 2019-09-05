package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.infra.LoadCertificateFromUriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
//@RequestMapping(value = "/vdf/pulse", produces= MediaType.APPLICATION_JSON_VALUE)
class VdfPulseRetrieverResource {

    private final VdfPulseService vdfPulseService;

    @Autowired
    VdfPulseRetrieverResource(VdfPulseService vdfPulseService) {
        this.vdfPulseService = vdfPulseService;
    }

    @PostMapping("/vdf/pulse")
    ResponseEntity postSeed(@Valid @RequestBody VdfPulseDto newVdfPulse) {
        try {

            if (!vdfPulseService.isOpen()){
                return new ResponseEntity("Not open", HttpStatus.BAD_REQUEST);
            }


            if (validateSignature(newVdfPulse)){
                vdfPulseService.addSeed(newVdfPulse);
            } else {
                new ResponseEntity("Not allowed", HttpStatus.FORBIDDEN);

            }

        } catch (Exception e) {
            e.printStackTrace();
            new ResponseEntity("Internal server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        vdfPulseService.addSeed(newVdfPulse);
        return new ResponseEntity("Created", HttpStatus.CREATED);

    }

    private boolean validateSignature(final VdfPulseDto newVdfPulse) throws Exception {
        String baseUrl = "http://localhost:8080/beacon/2.0/certificate/";
        URL CERT_URL = new URL(baseUrl + newVdfPulse.getCertificateId());

        X509Certificate x509Certificate = LoadCertificateFromUriService.loadCert(CERT_URL);
        PublicKey publicKey = x509Certificate.getPublicKey();

        final ICipherSuite suite = CipherSuiteBuilder.build(newVdfPulse.getCipherSuite());
        return suite.verify(publicKey, newVdfPulse.getSignatureValue(), VdfPulseSerialize.serializeVdfDto(newVdfPulse));
    }

}
