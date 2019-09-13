package com.example.beacon.vdf.application.vdfunicorn;

import com.example.beacon.vdf.application.VdfSeedDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class UnicornCurrentDto {

    @JsonProperty("status")
    private String statusEnum;
    private String start;
    private String end;
    private List<VdfSeedDto> seedList = new ArrayList<>();
    private String currentHash;
    private long nextRunInMinutes;

//    public UnicornCurrentDto(){
//        this.submissionTime = new SubmissionTime(DateUtil.getTimestampOfNextRun(ZonedDateTime.now()), 15);
//        this.currentHash = "";
//    }

    public void addSeed(VdfSeedDto seed){
        this.seedList.add(seed);
    }

}
