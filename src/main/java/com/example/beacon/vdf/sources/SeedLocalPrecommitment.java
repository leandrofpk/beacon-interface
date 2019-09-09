package com.example.beacon.vdf.sources;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CipherSuiteZero;
import com.example.beacon.shared.EntropyRepository;
import com.example.beacon.vdf.application.vdfpublic.SeedPostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class SeedLocalPrecommitment implements SeedInterface{

    private final EntropyRepository entropyRepository;

    private static final String DESCRIPTION = "Local Precommitment";

    @Autowired
    public SeedLocalPrecommitment(EntropyRepository entropyRepository) {
        this.entropyRepository = entropyRepository;
    }

    @Override
    public SeedPostDto getSeed() {
        ZonedDateTime timeStamp = entropyRepository.findNewerNumber();
        if (timeStamp!=null){
            return new SeedPostDto(entropyRepository.findByTimeStamp(timeStamp).getRawData(), DESCRIPTION);
        } else {
            return new SeedPostDto(CipherSuiteBuilder.build(0).getDigest("0"), DESCRIPTION);
        }
    }
}
