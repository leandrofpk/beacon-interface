package com.example.beacon.shared;

import com.example.beacon.interfac.infra.EntropyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface EntropyRepository extends JpaRepository<EntropyEntity, Integer> {

    @Query(value = "select max(e.timeStamp) from EntropyEntity e")
    ZonedDateTime findNewerNumber();

    @Query(value = "from EntropyEntity e where timeStamp = ?1")
    EntropyEntity findByTimeStamp(ZonedDateTime timeStamp);
}
