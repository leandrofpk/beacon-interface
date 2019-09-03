package com.example.beacon.interfac.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping(value = "/beacon/2.0/certificate", produces= MediaType.APPLICATION_JSON_VALUE)
public class CertificateResource {

    @GetMapping(path = "/{certificateIdentifier}")
    @ResponseBody
    public ResponseEntity getCertificate(@PathVariable String certificateIdentifier) {
        String hash = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

        if (hash.equals(certificateIdentifier)){
            return new ResponseEntity(getCertificateString(), HttpStatus.OK);
        } else {
            return new ResponseEntity("Certificate Not Found.", HttpStatus.NOT_FOUND);

        }

    }

    private String getCertificateString(){

        return "-----BEGIN CERTIFICATE-----\n" +
                "MIIGGTCCBAECFDcCQksITu8pQTtKbzCXx4dSeyjsMA0GCSqGSIb3DQEBCwUAMIHI\n" +
                "MQswCQYDVQQGEwJCUjEXMBUGA1UECAwOUmlvIGRlIEphbmVpcm8xGDAWBgNVBAcM\n" +
                "D0R1cXVlIGRlIENheGlhczFIMEYGA1UECgw/SW5zdGl0dXRvIE5hY2lvbmFsIGRl\n" +
                "IE1ldHJvbG9naWEgUXVhbGlkYWRlIGUgVGVjbm9sb2dpYSBJTk1FVFJPMRcwFQYD\n" +
                "VQQDDA5pbm1ldHJvLmdvdi5icjEjMCEGCSqGSIb3DQEJARYUbGFpbmZAaW5tZXRy\n" +
                "by5nb3YuYnIwHhcNMTkwODE0MDIzMzM2WhcNMjIwODEzMDIzMzM2WjCByDELMAkG\n" +
                "A1UEBhMCQlIxFzAVBgNVBAgMDlJpbyBkZSBKYW5laXJvMRgwFgYDVQQHDA9EdXF1\n" +
                "ZSBkZSBDYXhpYXMxSDBGBgNVBAoMP0luc3RpdHV0byBOYWNpb25hbCBkZSBNZXRy\n" +
                "b2xvZ2lhIFF1YWxpZGFkZSBlIFRlY25vbG9naWEgSU5NRVRSTzEXMBUGA1UEAwwO\n" +
                "aW5tZXRyby5nb3YuYnIxIzAhBgkqhkiG9w0BCQEWFGxhaW5mQGlubWV0cm8uZ292\n" +
                "LmJyMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAtkgtb6B05KPqd3Qd\n" +
                "CbudT216G1g26K2inBIXyVkKNnB0lWgr74NhJ03D32NWKZ70Ceh0PBHpUV7uYGCE\n" +
                "O+TGaEV/yn01suHrfzT7CT9Dbxz5gikdDuZgkYGGN/8LdYcPv1iP63LnBobgAmQo\n" +
                "zWd2NZwhSuGgQEYMHCp4pOXfSRaQmZ1vMkASvGgW+NsFx3QGd7GYmPKuzRnWhRXz\n" +
                "WG/qeJ7/3+Ek6q8K4FEjSz/etSRYBHkS38UTLNol8sV3kyDPh+oTFzgzBbQQ4SWe\n" +
                "/8z6Ym53CauRLuFekPjwea6f8iMefH1EnKMaKEx+P4iK+DjNEoQCb/oxzHXs/XFp\n" +
                "CuoK37O8Z7CeqnbO89UOcn+Ml/NqEW1Nt6Wdbg7v0GeClWW21c7dc2X0EOoEfiPl\n" +
                "HemScQQouqsFIMeLGiPmmKH+zwUDr98d87Dw54a+uQGWZHsN5+N2epjstjNrbB+h\n" +
                "pqz02VG4QcGTLqyct5ldhPkGDo0pdmhk7fmE48cGnDmoeY5i0h2Anl60TNxmwwsi\n" +
                "299M5lmL9The3rNMxLRhDtFffPPiTq/4Uq6uvuo0v7fukbtir05TNmgKe6c0u0HO\n" +
                "rh43d5tBITKmuMqIypRTEZNF0OZorjfrsHaSR9ABmzn9ElbH0yHqyJbLZT4igYNp\n" +
                "GmscC/ksooZhXorIAq/cS0T4ak8CAwEAATANBgkqhkiG9w0BAQsFAAOCAgEAFrFI\n" +
                "bD78RNCuI2BcYk7MeJ89YY6HkYEstMiRzGPUAHGn/xJr9nWmWP8TwrDvaudoHWN4\n" +
                "tDcTNyWZM+uYP6ps91LGp0dCHSBIt3Hukvo/6vSwMfA7nPC12mpUWK/7RJzD4Cu5\n" +
                "v2T1lMwRm/WRXfTxOJWn5yzxUK3xZ09CQScVrJCIT9SptQQoocEes0o8iLX6b6EC\n" +
                "S/yHvBBrhl/VlJvyP12xUbD6WoJ+UNDkKhLueHuiXwlg57CRsLK9eV/I5i3dbscb\n" +
                "WkwmysAWNEQkBOYIwsWjbvHPIuIKKdle8OUsKpO9bxacfUxyPArvQjP6UAd0Bakm\n" +
                "7wZjbmj2zeoQtg8XnOlhUz0YVUeR3Uxg4pjaftTOMQDX3EgisxoJ2Q12oSpaoQQd\n" +
                "Ax60BqOcKjU72y7ruAnz/thQFSg0mUVKVfaMLVnxpEKASeIO949JI/N480ObO24y\n" +
                "DqvWBTnbyEDuFP8JhhFXOj/WqTYnB+bg4LfXmWi4L+1diBG+XwwqBRIWUNb2TkEY\n" +
                "W0pSKiQPao/QsseM1lcDBO2WhxulcbijkM20mgbr5YO9KrbPzbJLRC9KrKShqtZg\n" +
                "MbIkkGQgh/yjhdjVnHAOwx9ysunhJY5tXVCSP7vhzYD2s2B0gRy8CN4KoSHCxm3U\n" +
                "iyMifCq921SLYhxQWU775SLUuySKnaIIKewF1yE=\n" +
                "-----END CERTIFICATE-----";

    }

}
