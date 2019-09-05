package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.interfac.api.dto.PulseDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VdfPulseScheduling {

    private final VdfPulseService vdfPulseService;

    public VdfPulseScheduling(VdfPulseService vdfPulseService) {
        this.vdfPulseService = vdfPulseService;
    }

//    @Scheduled(cron = "00 * * * * *")
    @Scheduled(cron = "0 34 * * * *")
    private void startTimeSlot(){
        vdfPulseService.startTimeSlot();

//        vdfPulseService.addSeed(new VdfPulseDto());
    }

    //    @Scheduled(cron = "0 0-5-10-15-20-25-30-35-40-45-50-55 * * * *") // a cada 5 min
    @Scheduled(cron = "5 34 * * * *")
    private void endTimeSlot() throws Exception {
        vdfPulseService.endTimeSlot();
    }

}
