package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import com.example.beacon.vdf.infra.entity.PrecomEntity;
import com.example.beacon.vdf.repository.PrecomRepository;
import com.example.beacon.vdf.sources.SeedLocalPrecommitmentUnicorn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
@EnableScheduling
public class VdfUnicornScheduling {

    private final VdfUnicornService vdfUnicornService;

    @Autowired
    public VdfUnicornScheduling(VdfUnicornService vdfUnicornService, PrecomRepository precomRepository
                                ) {
        this.vdfUnicornService = vdfUnicornService;
    }

    @Scheduled(cron = "${beacon.unicorn.start.submission}")
    public void startTimeSlot() {
        vdfUnicornService.startTimeSlot();
    }

//    @Scheduled(cron = "${beacon.unicorn.end.submission}")
//    public void endTimeSlot() throws Exception {
//        if (!vdfUnicornService.isOpen()){
//            return;
//        }
//
//        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
//        System.out.println(now);
//        PrecomEntity byTimeStamp = precomRepository.findByTimeStamp(now);
//
//
//        if (byTimeStamp != null) {
//            seedLocalPrecommitmentUnicorn.setPrecommitment(byTimeStamp.getPrecommitment());
//            vdfUnicornService.endTimeSlot();
//        }
//    }

}
