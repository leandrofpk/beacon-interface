package com.example.beacon.vdf.application.vdfpublic;

import com.example.beacon.vdf.SubmissionTime;
import com.example.beacon.vdf.infra.util.DateUtil;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Vdf {

    private String statusEnum;

    private SubmissionTime submissionTime;

    private List<VdfSeed> seedList = new ArrayList<>();

    private String currentHash;

    public Vdf(){
        this.submissionTime = new SubmissionTime(DateUtil.getTimestampOfNextRun(ZonedDateTime.now()), 15);
        this.currentHash = "";
    }

}
