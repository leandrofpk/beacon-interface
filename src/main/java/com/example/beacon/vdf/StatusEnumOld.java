package com.example.beacon.vdf;

public enum StatusEnumOld {

    OPEN("Open"),
    RUNNING("Running"),
    CLOSED("Closed");

    private String description;

    StatusEnumOld(String descricao) {
        this.description = descricao;
    }

    public String getDescription() {
        return description;
    }

}
