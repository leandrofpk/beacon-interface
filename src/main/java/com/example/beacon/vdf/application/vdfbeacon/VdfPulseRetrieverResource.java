package com.example.beacon.vdf.application.vdfbeacon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/vdf/pulse", produces= MediaType.APPLICATION_JSON_VALUE)
class VdfPulseRetrieverResource {

    private final VdfPulseService vdfPulseService;

    @Autowired
    VdfPulseRetrieverResource(VdfPulseService vdfPulseService) {
        this.vdfPulseService = vdfPulseService;
    }

    @PostMapping
    ResponseEntity postSeed(@Valid @RequestBody VdfPulseDto newVdfPulse){
        if (!vdfPulseService.isOpen()){
            return new ResponseEntity("Not open", HttpStatus.BAD_REQUEST);
        }

        validateSignature(newVdfPulse);

        vdfPulseService.addSeed(newVdfPulse);
        return new ResponseEntity("Created", HttpStatus.CREATED);

    }

    private void validateSignature(VdfPulseDto newVdfPulse) {
        System.out.println("Deve validar a assinatura");
    }

}
