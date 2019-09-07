package com.example.beacon.vdf.application.vdfbeacon.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeName("sloth")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class VdfSlothReturnVerifiedDto {

    private String y;

    private String x;

    private int iterations;

    private boolean verified;

    public VdfSlothReturnVerifiedDto(String x, String y, int iterations, boolean verified) {
        this.x = x;
        this.y = y;
        this.iterations = iterations;
        this.verified = verified;
    }
}
