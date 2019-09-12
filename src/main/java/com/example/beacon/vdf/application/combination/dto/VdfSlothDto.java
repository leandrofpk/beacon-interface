package com.example.beacon.vdf.application.combination.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VdfSlothDto {

    private String p;

    private String x;

    private String y;

    private int iterations;

    public VdfSlothDto(String p, String x, String y, int iterations) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.iterations = iterations;
    }
}
