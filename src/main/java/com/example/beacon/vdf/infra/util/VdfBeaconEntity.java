package com.example.beacon.vdf.infra.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "vdf_beacon")
@NoArgsConstructor
public class VdfBeaconEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String timestamp;

    private String signatureValue;

    private String outputValue;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "VdfBeaconEntity", cascade = CascadeType.PERSIST)
    private List<VdfBeaconSeedEntity> seedList;



}
