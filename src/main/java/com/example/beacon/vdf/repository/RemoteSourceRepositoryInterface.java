package com.example.beacon.vdf.repository;

import com.example.beacon.interfac.api.dto.PulseDto;

public interface RemoteSourceRepositoryInterface {
    PulseDto get(String uri);
}
