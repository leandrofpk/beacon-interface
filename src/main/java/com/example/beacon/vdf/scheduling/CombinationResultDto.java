package com.example.beacon.vdf.scheduling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    DTO used to be transmitted via rabbitmq
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class CombinationResultDto {
    private String timeStamp;
    private String outputValue;
    private String uri;
}
