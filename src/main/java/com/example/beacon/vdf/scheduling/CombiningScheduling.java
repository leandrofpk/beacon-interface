package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.combination.CombinationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CombiningScheduling implements VdfSchedulingInterface{

    private final CombinationService combinationService;

    public CombiningScheduling(CombinationService combinationService) {
        this.combinationService = combinationService;
    }

//    @Scheduled(cron = "${beacon.combination.start.submission}")
    public void startTimeSlot(){
//        combinationService.startTimeSlot();
//        System.out.println("start");
    }
//
//    @Scheduled(cron = "${beacon.combination.end.submission}")
    public void endTimeSlot() throws Exception {
//        if (combinationService.isOpen()){
//            combinationService.endTimeSlot();
//            System.out.println("end");
//
//        }
    }

}
