package com.example.beaconinterface.vdf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VdfResource {

    private final VdfAppService vdfAppService;

    @Autowired
    public VdfResource(VdfAppService vdfAppService) {
        this.vdfAppService = vdfAppService;
    }

    public ResponseEntity submission(){

        vdfAppService.

        return null;
    }

}
