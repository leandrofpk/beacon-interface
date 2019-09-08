package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.vdfbeacon.VdfPulseService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VdfPulseScheduling implements VdfSchedulingInterface{

    private final VdfPulseService vdfPulseService;

    public VdfPulseScheduling(VdfPulseService vdfPulseService) {
        this.vdfPulseService = vdfPulseService;
    }

    @Scheduled(cron = "${vdf.pulse.start.submission}")
    public void startTimeSlot(){
        vdfPulseService.startTimeSlot();
        System.out.println("start");
    }

    @Scheduled(cron = "${vdf.pulse.end.submission}")
    public void endTimeSlot() throws Exception {
        if (vdfPulseService.isOpen()){
            vdfPulseService.endTimeSlot();
            System.out.println("end");

        }
    }

}
