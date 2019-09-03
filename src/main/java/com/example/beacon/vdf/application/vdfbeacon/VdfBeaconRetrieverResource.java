package com.example.beacon.vdf.application.vdfbeacon;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/vdf/beacon", produces= MediaType.APPLICATION_JSON_VALUE)
public class VdfBeaconRetrieverResource {


    public ResponseEntity getSeed(){
        return null;
    }


}
