package com.example.beacon.vdf.application.vdfbeacon;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VdfSlothDto {

    private String x;

    private String y;

    private int iterations;

    public VdfSlothDto(String x, String y, int iterations) {
        this.x = x;
        this.y = y;
        this.iterations = iterations;
    }
}
