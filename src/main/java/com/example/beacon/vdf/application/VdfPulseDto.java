package com.example.beacon.vdf.application;

import com.example.beacon.vdf.application.combination.dto.VdfSlothDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"uri", "version","certificateId", "cipherSuite", "pulseIndex","timeStamp","period","combination","seedList","slothDto","signatureValue","outputValue"})
/*
    Return to api or page
 */
public class VdfPulseDto {

    private String uri;

    private String version;

    private String certificateId;

    private int cipherSuite;

    private long pulseIndex;

    private String timeStamp;

    private int period;

    private String combination;

    private List<VdfSeedDto> seedList = new ArrayList<>();

    @JsonProperty("sloth")
    private VdfSlothDto slothDto;

    public void addSeed(VdfSeedDto seedDto){
        this.seedList.add(seedDto);
    }

    private String signatureValue;

    private String outputValue;

}
