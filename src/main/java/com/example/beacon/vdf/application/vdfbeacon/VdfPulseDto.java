package com.example.beacon.vdf.application.vdfbeacon;

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
public class VdfPulseDto {

    private String certificateId;

    private int cipherSuite;

    private long pulseIndex;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private String timeStamp;

    private String signatureValue;

    private int period;

    private List<VdfSeedDto> seedList = new ArrayList<>();

    @JsonProperty("sloth")
    private VdfSlothDto slothDto;

    private int statusCode;

    public void addSeed(VdfSeedDto seedDto){
        this.seedList.add(seedDto);
    }

}
