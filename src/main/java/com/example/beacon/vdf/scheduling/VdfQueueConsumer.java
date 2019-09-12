package com.example.beacon.vdf.scheduling;

import com.example.beacon.vdf.application.combination.CombinationService;
import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import com.example.beacon.vdf.sources.SeedLocalPrecommitment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Component
@Transactional
public class VdfQueueConsumer {

    private final CombinationService combinationService;

    private final VdfUnicornService vdfUnicornService;

    private final SeedLocalPrecommitment seedLocalPrecommitment;

    private final Environment env;

    private static final Logger logger = LoggerFactory.getLogger(VdfQueueConsumer.class);

    @Autowired
    public VdfQueueConsumer(CombinationService combinationService, VdfUnicornService vdfUnicornService, SeedLocalPrecommitment seedLocalPrecommitment, Environment env) {
        this.combinationService = combinationService;
        this.vdfUnicornService = vdfUnicornService;
        this.seedLocalPrecommitment = seedLocalPrecommitment;
        this.env = env;
    }

    @RabbitListener(queues = {"pulses_combination_queue"})
    public void receiveCombination(PrecommitmentQueueDto dto) throws Exception {
        logger.warn("PrecommitmentQueueDto received:  " + dto);
        seedLocalPrecommitment.setPrecommitment(dto.getPrecommitment());
        logger.warn("Start combination: " + ZonedDateTime.now());
        combinationService.run(dto.getTimeStamp());
        logger.warn(String.format("combination released: %s - iterations: %s",  dto.getTimeStamp(), env.getProperty("beacon.combination.iterations")));
    }

    @RabbitListener(queues = {"pulses_unicorn_queue"})
    public void receiveUnicorn(PrecommitmentQueueDto dto) {
        System.out.println("UNICORN-QUEUE: NAO IMPLEMENTADA");
        System.out.println(dto);
    }

}
