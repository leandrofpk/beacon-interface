package com.example.beacon.vdf.application.old;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Data
public class VdfSeed {

    private ZonedDateTime timestamp;

    private String seed;

    private String uri;

    public VdfSeed(ZonedDateTime timestamp, String seed, String uri) {
        this.timestamp = timestamp;
        this.seed = seed;
        this.uri = uri;
    }
}
