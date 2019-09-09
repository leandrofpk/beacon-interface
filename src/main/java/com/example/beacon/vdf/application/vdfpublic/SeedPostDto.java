package com.example.beacon.vdf.application.vdfpublic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeedPostDto {

    @NotNull
    public String seed;
    public String description;
}
