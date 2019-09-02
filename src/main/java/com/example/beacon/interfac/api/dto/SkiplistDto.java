package com.example.beacon.interfac.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkiplistDto {
    private List<PulseDto> skiplist;
}
