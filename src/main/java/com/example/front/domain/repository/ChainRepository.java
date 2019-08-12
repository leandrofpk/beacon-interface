package com.example.front.domain.repository;

import com.example.front.infra.ChainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChainRepository extends JpaRepository<ChainEntity, Integer> {
    ChainEntity findTop1ByActive(boolean active);
}
