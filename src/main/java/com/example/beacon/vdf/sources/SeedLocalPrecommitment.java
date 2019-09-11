package com.example.beacon.vdf.sources;

import org.springframework.stereotype.Component;

@Component
public class SeedLocalPrecommitment implements SeedInterface{

    private static final String DESCRIPTION = "Local Precommitment";

    private String precommitment;

    public void setPrecommitment(String precommitment){
        this.precommitment = precommitment;
    }

    @Override
    public SeedSourceDto getSeed() {
        SeedSourceDto seedSourceDto = new SeedSourceDto("", this.precommitment, DESCRIPTION);
        this.precommitment = "";
        return seedSourceDto;

    }
}
