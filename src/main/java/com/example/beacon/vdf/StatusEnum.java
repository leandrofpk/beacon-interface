package com.example.beacon.vdf;

public enum StatusEnum {

    OPEN("Open"),
    RUNNING("Running"),
    CLOSED("Closed");

    private String description;

    StatusEnum(String descricao) {
        this.description = descricao;
    }

    public String getDescription() {
        return description;
    }

}
