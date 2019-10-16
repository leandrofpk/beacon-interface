package com.example.beacon.vdf.sources;

import com.example.beacon.vdf.repository.BeaconRemoteDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SeedLastChile implements SeedInterface {

    private static final String DESCRIPTION = "Last precommitment Chile";

    @Override
    public SeedSourceDto getSeed() {
        RestTemplate restTemplate = new RestTemplate();
        BeaconRemoteDto lastPulse = restTemplate.getForObject("https://random.uchile.cl/beacon/2.0/pulse/last", BeaconRemoteDto.class);
        return new SeedSourceDto(lastPulse.getTimeStamp(), lastPulse.getUri(),
                lastPulse.getPrecommitmentValue(), DESCRIPTION, SeedLastChile.class);
    }
}
