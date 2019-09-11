package com.example.beacon.vdf;

import com.example.beacon.interfac.api.dto.EntropyDto;
import com.example.beacon.vdf.application.combination.CombinationService;
import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import com.example.beacon.vdf.sources.SeedLocalPrecommitment;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class VdfQueueConsumer {

    private final CombinationService combinationService;

    private final VdfUnicornService vdfUnicornService;

    private final SeedLocalPrecommitment seedLocalPrecommitment;

    @Autowired
    public VdfQueueConsumer(CombinationService combinationService, VdfUnicornService vdfUnicornService, SeedLocalPrecommitment seedLocalPrecommitment) {
        this.combinationService = combinationService;
        this.vdfUnicornService = vdfUnicornService;
        this.seedLocalPrecommitment = seedLocalPrecommitment;
    }

    @RabbitListener(queues = {"pulses_combination_queue"})
    public void receiveCombination(EntropyDto entropyDto) throws Exception {
        seedLocalPrecommitment.setPrecommitment(entropyDto.getRawData());
        combinationService.run();
    }

    @RabbitListener(queues = {"pulses_unicorn_queue"})
    public void receiveUnicorn(List<EntropyDto> list) {


        System.out.println("UNICORN-QUEUE: NAO IMPLEMENTADA");
    }

}
