package com.example.beacon.vdf.application.vdfbeacon;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VdfPulseScheduling {

    private final VdfPulseService vdfPulseService;

    public VdfPulseScheduling(VdfPulseService vdfPulseService) {
        this.vdfPulseService = vdfPulseService;
    }

    @Scheduled(cron = "${vdf.pulse.start.submission}")
    private void startTimeSlot(){
        vdfPulseService.startTimeSlot();
        System.out.println("start");
    }

//    @Scheduled(cron = "10 55 * * * *")
//    @Scheduled(cron = "0 1,6,11,16,21,26,31,36,41,46,51,56 * * * *") // a cada 5 min
    @Scheduled(cron = "${vdf.pulse.end.submission}")
    private void endTimeSlot() throws Exception {
        if (vdfPulseService.isOpen()){
            vdfPulseService.endTimeSlot();
            System.out.println("end");

        }
    }

}
