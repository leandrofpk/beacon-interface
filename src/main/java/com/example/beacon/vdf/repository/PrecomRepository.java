package com.example.beacon.vdf.repository;

import com.example.beacon.vdf.infra.entity.PrecomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface PrecomRepository extends JpaRepository<PrecomEntity, Long> {
    long deleteByTimeStamp(ZonedDateTime timeStamp);
    PrecomEntity findByTimeStamp(ZonedDateTime timeStamp);
}
