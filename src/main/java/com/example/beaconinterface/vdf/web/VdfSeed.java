package com.example.beaconinterface.vdf.web;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class VdfSeed {

    private final ZonedDateTime timestamp;

    private final String seed;

    private final String uri;

    public VdfSeed(ZonedDateTime timestamp, String seed, String uri) {
        this.timestamp = timestamp;
        this.seed = seed;
        this.uri = uri;
    }
}
