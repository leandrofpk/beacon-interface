package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.vdfpublic.VdfPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@EnableScheduling
public class VdfPublicScheduling implements VdfSchedulingInterface{

    private final VdfPublicService vdfPublicService;

    @Autowired
    public VdfPublicScheduling(VdfPublicService vdfPublicService) {
        this.vdfPublicService = vdfPublicService;
    }

    @Scheduled(cron = "${vdf.public.start.submission}")
    public void startTimeSlot() throws Exception {
        System.out.println("START submission:" + ZonedDateTime.now());
        vdfPublicService.startTimeSlot();
    }

    @Scheduled(cron = "${vdf.public.end.submission}")
    public void endTimeSlot() throws Exception {
        System.out.println("END submission:" + ZonedDateTime.now());
        if (vdfPublicService.isOpen()){
            vdfPublicService.endTimeSlot();
        }
    }

}
