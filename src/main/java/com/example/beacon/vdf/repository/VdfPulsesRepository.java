package com.example.beacon.vdf.repository;

import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface VdfPulsesRepository extends JpaRepository<VdfPulseEntity, Long> {

    @Query(value = "SELECT * FROM vdf_pulse pulse where pulse.time_stamp > ?1 order by pulse.time_stamp limit 1;", nativeQuery = true)
    VdfPulseEntity findNext(ZonedDateTime timeStamp);

    @Query(value = "SELECT * FROM vdf_pulse pulse where pulse.time_stamp < ?1 order by pulse.time_stamp desc limit 1;", nativeQuery = true)
    VdfPulseEntity findPrevious(ZonedDateTime timeStamp);

    @Query(value = "SELECT distinct p from VdfPulseEntity p left join fetch p.seedList l where p.timeStamp >= ?1 and p.timeStamp <=?2 order by p.timeStamp")
    List<VdfPulseEntity> findSequence(ZonedDateTime timeStamp1, ZonedDateTime timeStamp2);

    @Query(value = "select distinct max(p.pulseIndex) from VdfPulseEntity p")
    Long findMaxId();

    @Query(value = "select p from VdfPulseEntity p left join fetch p.seedList where p.pulseIndex = ?1")
    VdfPulseEntity findByPulseIndex(Long pulseIndex);

    @Query(value = "select min(p.pulseIndex) from VdfPulseEntity p")
    Long findFirst();

    @Query(value = "select p from VdfPulseEntity p left join fetch p.seedList where p.timeStamp = ?1")
    VdfPulseEntity findByTimeStamp(ZonedDateTime timeStamp);
}
