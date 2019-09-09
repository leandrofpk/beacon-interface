package com.example.beacon.vdf.infra.entity;

import com.example.beacon.vdf.application.vdfpublic.SeedPostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "vdf_seed")
@NoArgsConstructor
@AllArgsConstructor
public class VdfSeedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seed;

    private String origin;

    @ManyToOne
    @JoinColumn(name = "vdf_pulse_id")
    private VdfPulseEntity vdfPulseEntity;

    public VdfSeedEntity(SeedPostDto dto, VdfPulseEntity vdfPulseEntity) {
        this.seed = dto.getSeed();
        this.origin = dto.getDescription();
        this.vdfPulseEntity = vdfPulseEntity;
    }
}
