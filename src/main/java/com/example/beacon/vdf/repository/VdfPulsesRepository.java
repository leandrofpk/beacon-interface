package com.example.beacon.vdf.repository;

import com.example.beacon.interfac.domain.repository.PulsesQueries;
import com.example.beacon.interfac.infra.PulseEntity;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface VdfPulsesRepository extends JpaRepository<VdfPulseEntity, Long> {

    @Query(value = "SELECT * FROM vdf_pulse where pulse.time_stamp > ?1 order by pulse.time_stamp limit 1;", nativeQuery = true)
    PulseEntity findNext(ZonedDateTime timeStamp);

    @Query(value = "SELECT * FROM vdf_pulse where pulse.time_stamp < ?1 order by pulse.time_stamp desc limit 1;", nativeQuery = true)
    PulseEntity findPrevious(ZonedDateTime timeStamp);

    @Query(value = "SELECT distinct p from VdfPulseEntity p left join fetch p.seedList l where p.timeStamp >= ?1 and p.timeStamp <=?2 order by p.timeStamp")
    List<PulseEntity> findSequence(ZonedDateTime timeStamp1, ZonedDateTime timeStamp2);

    @Query(value = "select distinct max(p.pulseIndex) from VdfPulseEntity p")
    Long findMaxId();

    VdfPulseEntity findByPulseIndex(Long pulseIndex);
}
