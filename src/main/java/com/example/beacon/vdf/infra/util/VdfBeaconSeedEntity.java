package com.example.beacon.vdf.infra.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "vdf_beacon_seed")
@NoArgsConstructor
public class VdfBeaconSeedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String seed;

    @ManyToOne
    @JoinColumn(name = "pulse_id")
    private VdfBeaconEntity pulseEntity;

}
