package com.example.beacon.vdf.sources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeedSourceDto {
    private String timeStamp;
    private String uri;
    private String seed;
    private String description;
    private Class classz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeedSourceDto that = (SeedSourceDto) o;
        return timeStamp.equals(that.timeStamp) &&
                uri.equals(that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp, uri);
    }
}
