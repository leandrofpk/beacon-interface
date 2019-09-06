package com.example.beacon.vdf.application.vdfbeacon;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class VdfPulseScheduling {

    private final VdfPulseService vdfPulseService;

    public VdfPulseScheduling(VdfPulseService vdfPulseService) {
        this.vdfPulseService = vdfPulseService;
    }

//    @Scheduled(cron = "00 * * * * *")
    @Scheduled(cron = "0 50 * * * *")
    private void startTimeSlot(){
        vdfPulseService.startTimeSlot();
        System.out.println("Start:" + ZonedDateTime.now());

//        vdfPulseService.addSeed(new VdfPulseDtoPost());
    }

    //    @Scheduled(cron = "0 0-5-10-15-20-25-30-35-40-45-50-55 * * * *") // a cada 5 min
    @Scheduled(cron = "10 50 * * * *")
    private void endTimeSlot() throws Exception {
        vdfPulseService.endTimeSlot();
    }

}
