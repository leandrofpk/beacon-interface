package com.example.beacon.vdf.application.combination.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VdfSeedDto {

    private String seed;

    private String timeStamp;

    private String description;

    private String uri;

    private String cumulativeHash;

}