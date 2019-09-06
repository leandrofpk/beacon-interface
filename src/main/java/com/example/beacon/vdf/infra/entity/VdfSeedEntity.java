package com.example.beacon.vdf.infra.entity;

import com.example.beacon.vdf.application.vdfbeacon.OriginEnum;
import com.example.beacon.vdf.application.vdfbeacon.VdfPulseDtoPost;
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

    @Enumerated(EnumType.STRING)
    private OriginEnum origin;

    @ManyToOne
    @JoinColumn(name = "vdf_pulse_id")
    private VdfPulseEntity vdfPulseEntity;

    public VdfSeedEntity(VdfPulseDtoPost dto, VdfPulseEntity vdfPulseEntity) {
        this.seed = dto.getSeed();
        this.origin = dto.getOriginEnum();
        this.vdfPulseEntity = vdfPulseEntity;
    }
}
