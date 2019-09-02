package com.example.beacon.interfac.domain.repository;

import com.example.beacon.interfac.infra.ChainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChainRepository extends JpaRepository<ChainEntity, Integer> {
    ChainEntity findTop1ByActive(boolean active);
}
