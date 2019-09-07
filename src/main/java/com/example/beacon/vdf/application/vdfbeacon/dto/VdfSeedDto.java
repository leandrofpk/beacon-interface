package com.example.beacon.vdf.application.vdfbeacon.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VdfSeedDto {

    private String seed;

    private String origin;

    public VdfSeedDto(String seed, String origin) {
        this.seed = seed;
        this.origin = origin;
    }
}
