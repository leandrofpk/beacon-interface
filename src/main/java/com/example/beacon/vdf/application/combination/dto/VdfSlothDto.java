package com.example.beacon.vdf.application.combination.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VdfSlothDto {

    private String p;

    private String x;

    private int iterations;

    private String y;

    public VdfSlothDto(String p, String x, int iterations, String y) {
        this.p = p;
        this.x = x;
        this.iterations = iterations;
        this.y = y;
    }
}
