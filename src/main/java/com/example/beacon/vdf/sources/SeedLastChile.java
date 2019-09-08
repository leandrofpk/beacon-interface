package com.example.beacon.vdf.sources;

import com.example.beacon.vdf.application.vdfpublic.SeedPostDto;
import com.example.beacon.vdf.repository.BeaconRemoteDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SeedLastChile implements SeedInterface {

    private static final String DESCRIPTION = "Last precommitment Chile";

    @Override
    public SeedPostDto getSeed() {
        RestTemplate restTemplate = new RestTemplate();
        BeaconRemoteDto lastPulse = restTemplate.getForObject("https://random.uchile.cl/beacon/2.0/pulse/last", BeaconRemoteDto.class);
        return new SeedPostDto(lastPulse.getPrecommitmentValue(),DESCRIPTION);
    }
}
