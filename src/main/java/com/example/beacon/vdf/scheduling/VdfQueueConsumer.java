package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.combination.CombinationService;
import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import com.example.beacon.vdf.sources.SeedLocalPrecommitment;
import com.example.beacon.vdf.sources.SeedLocalPrecommitmentUnicorn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
@Transactional
public class VdfQueueConsumer {

    private final CombinationService combinationService;

    private final VdfUnicornService vdfUnicornService;

    private final SeedLocalPrecommitment seedLocalPrecommitmentCombination;

    private final SeedLocalPrecommitmentUnicorn seedLocalPrecommitmentUnicorn;

    private final Environment env;

    private static final Logger logger = LoggerFactory.getLogger(VdfQueueConsumer.class);

    @Autowired
    public VdfQueueConsumer(CombinationService combinationService, VdfUnicornService vdfUnicornService, SeedLocalPrecommitment seedLocalPrecommitmentCombination, SeedLocalPrecommitmentUnicorn seedLocalPrecommitmentUnicorn, Environment env) {
        this.combinationService = combinationService;
        this.vdfUnicornService = vdfUnicornService;
        this.seedLocalPrecommitmentCombination = seedLocalPrecommitmentCombination;
        this.seedLocalPrecommitmentUnicorn = seedLocalPrecommitmentUnicorn;
        this.env = env;
    }

    @RabbitListener(queues = {"pulses_combination_queue"})
    public void receiveCombination(PrecommitmentQueueDto dto) throws Exception {
        ZonedDateTime parse = ZonedDateTime.parse(dto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        long between = ChronoUnit.MINUTES.between(parse, now);
        if (between==0){
            logger.warn("PrecommitmentQueueDto received:  " + dto);
            seedLocalPrecommitmentCombination.setPrecommitment(dto);
            logger.warn("Start combination: " + ZonedDateTime.now());
            combinationService.run(dto.getTimeStamp());
            logger.warn(String.format("combination released: %s - iterations: %s",  dto.getTimeStamp(), env.getProperty("beacon.combination.iterations")));
        } else {
            logger.warn("Discarded:" + dto);
        }
    }

    @RabbitListener(queues = {"pulses_unicorn_queue"})
    public void receiveUnicorn(PrecommitmentQueueDto dto) throws Exception {
        ZonedDateTime parse = ZonedDateTime.parse(dto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        long between = ChronoUnit.MINUTES.between(parse, now);

        if (!vdfUnicornService.isOpen()){
            return;
        }
        if (between==0){
            seedLocalPrecommitmentUnicorn.setPrecommitmentUnicorn(dto);
            vdfUnicornService.endTimeSlot();
        }

    }

}
