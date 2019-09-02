package com.example.beacon.vdf.application;

import com.example.beacon.interfac.api.dto.PulseDto;
import com.example.beacon.vdf.repository.RemoteSourceRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.ZonedDateTime;

@Service
@ApplicationScope
public class VdfAppService {

    private final Environment environment;

    private final RemoteSourceRepositoryInterface remoteSourceRepositoryInterface;

    private Vdf vdf;

    @Autowired
    public VdfAppService(Environment environment, RemoteSourceRepositoryInterface remoteSourceRepositoryInterface) {
        this.environment = environment;
        this.remoteSourceRepositoryInterface = remoteSourceRepositoryInterface;
        this.vdf = new Vdf();
    }

    public void startSubmissions(){
        vdf.startSubmissions(Integer.parseInt(environment.getProperty("vdf.submission.duration")));

        // apenas para validar
        PulseDto pulseNist = remoteSourceRepositoryInterface.get("https://beacon.nist.gov/beacon/2.0/pulse/last");
        vdf.addSeed(new VdfSeed(ZonedDateTime.now(),
                                pulseNist.getPrecommitmentValue(),
                                pulseNist.getUri()));

//        PulseDto pulseChile = remoteSourceRepositoryInterface.get("https://random.uchile.cl/beacon/2.0/pulse/last");
//        vdf.addSeed(new VdfSeed(ZonedDateTime.now(),
//                                pulseChile.getPrecommitmentValue(),
//                                pulseChile.getUri()));

    }

    public void run(){
        vdf.startProcessing();
    }

    public void addSeed(){

    }

    public Vdf getCurrentVdf(){
        return this.vdf;
    }

    public void getPreDefinedSeeds(){

        //nist
        //chile
    }

    public void getHonestParty(){

    }



}
