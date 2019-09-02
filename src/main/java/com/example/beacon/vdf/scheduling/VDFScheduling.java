package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.VdfAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@EnableScheduling
public class VDFScheduling {

    private final VdfAppService vdfAppService;

    @Autowired
    public VDFScheduling(VdfAppService vdfAppService) {
        this.vdfAppService = vdfAppService;
    }

//    @Scheduled(cron = "0 53 * * * *")
//    @Scheduled(cron = "0 46 * * * *")
//    @Scheduled(cron =  "${vdf.submission.start}")

    @Scheduled(cron = "0 00,30 * * * *")
    private void startNewSubmission(){
        System.out.println("START submission:" + ZonedDateTime.now());
        vdfAppService.startSubmissions();
    }

    @Scheduled(cron =  "0 15,45 * * * *")
    private void endSubmission(){
        System.out.println("END submission:" + ZonedDateTime.now());
        vdfAppService.run();
//        vdfAppService.
    }

}
