package com.example.beacon.vdf.application.vdfunicorn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeedPostDto {
    @NotEmpty(message = "this field cannot be blank")
    private String seed;
    private String description;
    private String uri;
}
