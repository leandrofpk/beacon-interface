package com.example.beacon.vdf.repository;

import com.example.beacon.interfac.api.dto.PulseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RemoteSourceRepositoryImpl implements RemoteSourceRepositoryInterface {

    @Deprecated //TODO remover
    public PulseDto get(String uri){
        RestTemplate restTemplate = new RestTemplate();
        PulseDto lastPulse = restTemplate.getForObject(uri, PulseDto.class);
        return lastPulse;
    }

}
