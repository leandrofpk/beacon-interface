package com.example.beacon.vdf.sources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeedSourceDto {
    private String timeStamp;
    private String uri;
    private String seed;
    private String description;
}
