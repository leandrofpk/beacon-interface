package com.example.beaconinterface.vdf;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SubmitTime {

    private ZonedDateTime start;

    private ZonedDateTime end;

}
