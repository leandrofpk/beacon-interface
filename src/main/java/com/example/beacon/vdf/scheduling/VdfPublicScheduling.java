package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@EnableScheduling
public class VdfPublicScheduling implements VdfSchedulingInterface{

    private final VdfUnicornService vdfUnicornService;

    @Autowired
    public VdfPublicScheduling(VdfUnicornService vdfUnicornService) {
        this.vdfUnicornService = vdfUnicornService;
    }

    @Scheduled(cron = "${beacon.unicorn.start.submission}")
    public void startTimeSlot() throws Exception {
        System.out.println("START submission:" + ZonedDateTime.now());
        vdfUnicornService.startTimeSlot();
    }

    @Scheduled(cron = "${beacon.unicorn.end.submission}")
    public void endTimeSlot() throws Exception {
        System.out.println("END submission:" + ZonedDateTime.now());
        if (vdfUnicornService.isOpen()){
            vdfUnicornService.endTimeSlot();
        }
    }

}
