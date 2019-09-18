package com.example.beacon.interfac.infra;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "chain")
public class ChainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String versionUri;

    @NotNull
    private String versionPulse;

    @NotNull
    private int cipherSuite;

    @NotNull
    private String cipherSuiteDescription;

    @NotNull
    private int period;

    @NotNull
    private long chainIndex;

    private boolean active;


}
