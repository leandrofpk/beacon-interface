package com.example.front.domain.repository;

import com.example.front.infra.PulseEntity;

import java.time.ZonedDateTime;

public interface PulsesQueries {
    PulseEntity last(Long chain);
    PulseEntity first(Long chain);
    PulseEntity findByChainAndPulseIndex(Long chainIndex, Long pulseIndex);
    PulseEntity findByTimestamp(ZonedDateTime timeStamp);
}
