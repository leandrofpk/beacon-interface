package com.example.beacon.vdf.application.vdfbeacon;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VdfPulseScheduling {

    private final VdfPulseService vdfPulseService;

    public VdfPulseScheduling(VdfPulseService vdfPulseService) {
        this.vdfPulseService = vdfPulseService;
    }

//    @Scheduled(cron = "00 * * * * *")
    @Scheduled(cron = "0 55 * * * *")
//    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 * * * *") // a cada 5 min
    private void startTimeSlot(){
        vdfPulseService.startTimeSlot();
    }

    @Scheduled(cron = "10 55 * * * *")
//    @Scheduled(cron = "0 1,6,11,16,21,26,31,36,41,46,51,56 * * * *") // a cada 5 min
    private void endTimeSlot() throws Exception {
        vdfPulseService.endTimeSlot();
    }

}
