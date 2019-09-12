package com.example.beacon.vdf.infra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "vdf_precom")
@Data @NoArgsConstructor @AllArgsConstructor
public class PrecomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ZonedDateTime timeStamp;

    @Column(name = "precommitment_value")
    private String precommitment;

}
