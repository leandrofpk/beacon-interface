package com.example.beacon.vdf.sources;

import com.example.beacon.vdf.scheduling.PrecommitmentQueueDto;
import org.springframework.stereotype.Component;

@Component
public class SeedLocalPrecommitmentUnicorn implements SeedInterface{

    private static final String DESCRIPTION = "Local Precommitment";

    private PrecommitmentQueueDto dto;

    public void setPrecommitmentUnicorn(PrecommitmentQueueDto dto){
        this.dto = dto;
    }

    @Override
    public SeedSourceDto getSeed() {
        SeedSourceDto seedSourceDto = new SeedSourceDto(dto.getUri(), dto.getPrecommitment(), DESCRIPTION);
        this.dto = null;
        return seedSourceDto;
    }
}
