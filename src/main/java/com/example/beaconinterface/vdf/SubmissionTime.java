package com.example.beaconinterface.vdf;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class SubmissionTime {

    private final ZonedDateTime start;

    private final ZonedDateTime end;

    private final StatusEnum statusEnum;

    public SubmissionTime(ZonedDateTime start, int submissioninMinutes, StatusEnum statusEnum) {
        this.start = start;
        this.end = start.plus(submissioninMinutes, ChronoUnit.MINUTES);
        this.statusEnum = statusEnum;
    }
}
