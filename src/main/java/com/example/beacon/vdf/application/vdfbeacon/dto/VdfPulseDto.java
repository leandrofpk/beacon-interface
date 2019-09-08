package com.example.beacon.vdf.application.vdfbeacon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonTypeName("pulse")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
/*
    Return to api or page
 */
public class VdfPulseDto {

    private String certificateId;

    private int cipherSuite;

    private long pulseIndex;

    private String timeStamp;

    private String signatureValue;

    private int period;

    private List<VdfSeedDto> seedList = new ArrayList<>();

    @JsonProperty("sloth")
    private VdfSlothDto slothDto;

    public void addSeed(VdfSeedDto seedDto){
        this.seedList.add(seedDto);
    }

}
