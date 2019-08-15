package com.example.beaconinterface.domain.repository;

import com.example.beaconinterface.infra.PulseEntity;

import java.time.ZonedDateTime;

public interface PulsesQueries {
    PulseEntity last(Long chain);
    PulseEntity first(Long chain);
    PulseEntity findByChainAndPulseIndex(Long chainIndex, Long pulseIndex);
    PulseEntity findByTimestamp(ZonedDateTime timeStamp);
}
