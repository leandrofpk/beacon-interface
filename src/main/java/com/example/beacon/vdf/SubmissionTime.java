package com.example.beacon.vdf;

import lombok.Data;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class SubmissionTime {

    private final ZonedDateTime start;

    private final ZonedDateTime end;

    public SubmissionTime(ZonedDateTime start, int submissioninMinutes) {
        this.start = start;
        this.end = start.plus(submissioninMinutes, ChronoUnit.MINUTES);
    }
}
