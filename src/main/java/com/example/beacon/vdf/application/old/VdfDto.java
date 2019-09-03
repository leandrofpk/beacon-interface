package com.example.beacon.vdf.application.old;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("vdf")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class VdfDto {

    private String start;

    private String end;

    private String startLocal;

    private String endLocal;

    private String status;

    private String currentHash;

    private long nextRunInMinutes;

    private String output;

    private List<VdfSeed> seedList;

}
