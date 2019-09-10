package com.example.beacon.vdf.sources;

import com.example.beacon.vdf.repository.BeaconRemoteDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SeedLastNist implements SeedInterface {

    private static final String DESCRIPTION = "Last precommitment NIST";

    @Override
    public SeedSourceDto getSeed() {
        RestTemplate restTemplate = new RestTemplate();
        BeaconRemoteDto lastPulse = restTemplate.getForObject("https://beacon.nist.gov/beacon/2.0/pulse/last", BeaconRemoteDto.class);
        return new SeedSourceDto(lastPulse.getUri(), lastPulse.getPrecommitmentValue(), DESCRIPTION);
    }
}
