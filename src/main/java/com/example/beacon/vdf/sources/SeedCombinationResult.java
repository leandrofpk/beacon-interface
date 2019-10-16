package com.example.beacon.vdf.sources;

import com.example.beacon.vdf.scheduling.CombinationResultDto;
import com.example.beacon.vdf.scheduling.PrecommitmentQueueDto;
import org.springframework.stereotype.Component;

@Component
public class SeedCombinationResult implements SeedInterface{

    private static final String DESCRIPTION = "Local Combination result";

    private CombinationResultDto dto;

    public void setCombinationResultDto(CombinationResultDto dto){
        this.dto = dto;
    }

    @Override
    public SeedSourceDto getSeed() {
        SeedSourceDto seedSourceDto = new SeedSourceDto(dto.getTimeStamp(), dto.getUri(),
                dto.getOutputValue(), DESCRIPTION, SeedCombinationResult.class);
        this.dto = null;
        return seedSourceDto;
    }
}
