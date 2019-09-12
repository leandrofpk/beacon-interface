package com.example.beacon.vdf.repository;

import com.example.beacon.vdf.infra.entity.CombinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface CombinationRepository extends JpaRepository<CombinationEntity, Long> {

    @Query(value = "SELECT * FROM combination c where c.time_stamp > ?1 order by c.time_stamp limit 1;", nativeQuery = true)
    CombinationEntity findNext(ZonedDateTime timeStamp);

    @Query(value = "SELECT * FROM combination c where c.time_stamp < ?1 order by c.time_stamp desc limit 1;", nativeQuery = true)
    CombinationEntity findPrevious(ZonedDateTime timeStamp);

    @Query(value = "SELECT distinct p from CombinationEntity p left join fetch p.seedList l where p.timeStamp >= ?1 and p.timeStamp <=?2 order by p.timeStamp")
    List<CombinationEntity> findSequence(ZonedDateTime timeStamp1, ZonedDateTime timeStamp2);

    @Query(value = "select distinct max(p.pulseIndex) from CombinationEntity p")
    Long findMaxId();

    @Query(value = "select p from CombinationEntity p left join fetch p.seedList where p.pulseIndex = ?1")
    CombinationEntity findByPulseIndex(Long pulseIndex);

    @Query(value = "select min(p.pulseIndex) from CombinationEntity p")
    Long findFirst();

    @Query(value = "select p from CombinationEntity p left join fetch p.seedList where p.timeStamp = ?1")
    CombinationEntity findByTimeStamp(ZonedDateTime timeStamp);
}
