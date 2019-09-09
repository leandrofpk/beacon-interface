package com.example.beacon.vdf.infra.entity;

import com.example.beacon.vdf.application.vdfpublic.SeedPostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "vdf_seed_public")
@NoArgsConstructor
@AllArgsConstructor
public class VdfSeedPublicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seed;

    private String description;

    @ManyToOne
    @JoinColumn(name = "vdf_public_id")
    private VdfPublicEntity vdfPublicEntity;

    public VdfSeedPublicEntity(SeedPostDto dto, VdfPublicEntity vdfPublicEntity) {
        this.seed = dto.getSeed();
        this.description = dto.getDescription();
        this.vdfPublicEntity = vdfPublicEntity;
    }
}
