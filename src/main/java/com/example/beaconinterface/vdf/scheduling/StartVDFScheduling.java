package com.example.beaconinterface.vdf.scheduling;

import com.example.beaconinterface.vdf.web.VdfAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@EnableScheduling
public class StartVDFScheduling {

    private final VdfAppService vdfAppService;

    @Autowired
    public StartVDFScheduling(VdfAppService vdfAppService) {
        this.vdfAppService = vdfAppService;
    }

//    @Scheduled(cron = "00 * * * * *")
//    @Scheduled(cron = "30 * * * * *")
    @Scheduled(cron = "* 00 * * * *")
    @Scheduled(cron = "* 30 * * * *")
    private void startNewSubmission(){
        System.out.println("START:" + ZonedDateTime.now());


    }

}
