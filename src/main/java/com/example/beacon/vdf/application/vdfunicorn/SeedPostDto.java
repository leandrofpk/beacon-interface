package com.example.beacon.vdf.application.vdfunicorn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeedPostDto {
    @NotNull
    private String seed;
    private String description;
    private String uri;
}
