package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class VdfUnicornScheduling {

    private final VdfUnicornService vdfUnicornService;

    @Autowired
    public VdfUnicornScheduling(VdfUnicornService vdfUnicornService) {
        this.vdfUnicornService = vdfUnicornService;
    }

    @Scheduled(cron = "${beacon.unicorn.start.submission}")
    public void startTimeSlot() {
        vdfUnicornService.startTimeSlot();
    }

}
