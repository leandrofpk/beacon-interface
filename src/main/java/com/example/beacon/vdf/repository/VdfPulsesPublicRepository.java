package com.example.beacon.vdf.repository;

import com.example.beacon.vdf.infra.entity.VdfPublicEntity;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface VdfPulsesPublicRepository extends JpaRepository<VdfPublicEntity, Long> {

    @Query(value = "SELECT * FROM vdf_public pulse where pulse.time_stamp > ?1 order by pulse.time_stamp limit 1;", nativeQuery = true)
    VdfPublicEntity findNext(ZonedDateTime timeStamp);

    @Query(value = "SELECT * FROM vdf_public pulse where pulse.time_stamp < ?1 order by pulse.time_stamp desc limit 1;", nativeQuery = true)
    VdfPublicEntity findPrevious(ZonedDateTime timeStamp);

    @Query(value = "SELECT distinct p from VdfPublicEntity p left join fetch p.seedList l where p.timeStamp >= ?1 and p.timeStamp <=?2 order by p.timeStamp")
    List<VdfPublicEntity> findSequence(ZonedDateTime timeStamp1, ZonedDateTime timeStamp2);

    @Query(value = "select distinct max(p.pulseIndex) from VdfPublicEntity p")
    Long findMaxId();

    @Query(value = "select p from VdfPublicEntity p left join fetch p.seedList where p.pulseIndex = ?1")
    VdfPublicEntity findByPulseIndex(Long pulseIndex);

    @Query(value = "select min(p.pulseIndex) from VdfPublicEntity p")
    Long findFirst();

    @Query(value = "select p from VdfPublicEntity p left join fetch p.seedList where p.timeStamp = ?1")
    VdfPublicEntity findByTimeStamp(ZonedDateTime timeStamp);
}
