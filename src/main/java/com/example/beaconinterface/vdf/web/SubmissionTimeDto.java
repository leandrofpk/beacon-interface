package com.example.beaconinterface.vdf.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionTimeDto {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private ZonedDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private ZonedDateTime end;

    private String status;

}
