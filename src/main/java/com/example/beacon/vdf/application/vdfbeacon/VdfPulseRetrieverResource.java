package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.vdfbeacon.dto.VdfPulseDtoPost;
import com.example.beacon.vdf.application.vdfbeacon.dto.VdfSlothReturnVerifiedDto;
import com.example.beacon.vdf.application.vdfpublic.SeedPostDto;
import com.example.beacon.vdf.infra.LoadCertificateFromUriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
class VdfPulseRetrieverResource {

    private final VdfPulseService vdfPulseService;

    @Autowired
    VdfPulseRetrieverResource(VdfPulseService vdfPulseService) {
        this.vdfPulseService = vdfPulseService;
    }

    @GetMapping("/beacon/2.0/pulse/vdf/verify")
    ResponseEntity verify(@RequestParam(name = "y") String y,
                          @RequestParam(name = "x") String x,
                          @RequestParam(name = "iterations") int iterations){

        boolean verified = VdfSloth.mod_verif(new BigInteger(y), new BigInteger(x), iterations);
        return new ResponseEntity(new VdfSlothReturnVerifiedDto(y, x, iterations, verified), HttpStatus.OK);
    }

    @PostMapping("/beacon/2.0/pulse/vdf")
    ResponseEntity postSeed(@Valid @RequestBody VdfPulseDtoPost newVdfPulse) {
        try {

            if (!vdfPulseService.isOpen()){
                return new ResponseEntity("Not open", HttpStatus.BAD_REQUEST);
            }


            if (validateSignature(newVdfPulse)){
                vdfPulseService.addSeed(new SeedPostDto(newVdfPulse.getSeed(), newVdfPulse.getOriginEnum().toString()));
            } else {
                new ResponseEntity("Not allowed", HttpStatus.FORBIDDEN);

            }

        } catch (Exception e) {
            e.printStackTrace();
            new ResponseEntity("Internal server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        vdfPulseService.addSeed(new SeedPostDto(newVdfPulse.getSeed(), newVdfPulse.getOriginEnum().toString()));
        return new ResponseEntity("Created", HttpStatus.CREATED);
    }

    private boolean validateSignature(final VdfPulseDtoPost newVdfPulse) throws Exception {
        String baseUrl = "http://localhost:8080/beacon/2.0/certificate/";
        URL CERT_URL = new URL(baseUrl + newVdfPulse.getCertificateId());

        X509Certificate x509Certificate = LoadCertificateFromUriService.loadCert(CERT_URL);
        PublicKey publicKey = x509Certificate.getPublicKey();

        final ICipherSuite suite = CipherSuiteBuilder.build(newVdfPulse.getCipherSuite());
        return suite.verify(publicKey, newVdfPulse.getSignatureValue(), VdfSerialize.serializeVdfDto(newVdfPulse));
    }

}
