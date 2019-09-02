package com.example.beacon.interfac.domain.pulse;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

//@Getter
@Data
@NoArgsConstructor
public class ListValue {

    private String uri;

    private String type;

    private String value;

    private ListValue(@NonNull String value, @NonNull String type, String uri) {
        this.value = value;
        this.type = type;
        this.uri = uri;
    }

    public static ListValue getOneValue(String value, String type, String uri){
        return new ListValue(value, type, uri);
    }

    @Override
    public String toString() {
        return "ListValue{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
