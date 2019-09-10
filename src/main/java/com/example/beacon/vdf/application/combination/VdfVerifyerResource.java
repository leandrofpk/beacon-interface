package com.example.beacon.vdf.application.combination;

import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.combination.dto.VdfSlothReturnVerifiedDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
class VdfVerifyerResource {

    @GetMapping("/beacon/2.0/pulse/vdf/verify")
    ResponseEntity verify(@RequestParam(name = "y") String y,
                          @RequestParam(name = "x") String x,
                          @RequestParam(name = "iterations") int iterations){

        boolean verified = VdfSloth.mod_verif(new BigInteger(y), new BigInteger(x), iterations);
        return new ResponseEntity(new VdfSlothReturnVerifiedDto(y, x, iterations, verified), HttpStatus.OK);
    }

}
