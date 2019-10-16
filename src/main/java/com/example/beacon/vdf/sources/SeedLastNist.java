package com.example.beacon.vdf.sources;

import com.example.beacon.vdf.repository.BeaconRemoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SeedLastNist implements SeedInterface {

    private final RestTemplate restTemplate;

    private static final String DESCRIPTION = "Last precommitment NIST";

    @Autowired
    public SeedLastNist(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public SeedSourceDto getSeed() {
//        RestTemplate restTemplate = new RestTemplate();
        BeaconRemoteDto lastPulse = restTemplate.getForObject("https://beacon.nist.gov/beacon/2.0/pulse/last", BeaconRemoteDto.class);
        return new SeedSourceDto(lastPulse.getTimeStamp(), lastPulse.getUri(), lastPulse.getPrecommitmentValue(),
                DESCRIPTION, SeedLastNist.class);
    }
}
