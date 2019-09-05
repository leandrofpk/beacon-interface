package com.example.beacon.vdf.infra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "vdf_pulse")
@NoArgsConstructor
@AllArgsConstructor
public class VdfPulseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificateId;

    private int cipherSuite;

    private long pulseIndex;

    private ZonedDateTime timeStamp;

    private String signatureValue;

    private int period;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vdfPulseEntity", cascade = CascadeType.ALL)
    private List<VdfSeedEntity> seedList = new ArrayList<>();

    private String x;

    private String y;

    private int iterations;

    private int statusCode;

    public void addSeed(VdfSeedEntity seed){
        this.seedList.add(seed);
    }

}
