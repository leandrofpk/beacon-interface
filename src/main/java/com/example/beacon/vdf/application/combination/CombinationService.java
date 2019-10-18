package com.example.beacon.vdf.application.combination;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.application.vdfunicorn.SeedPostDto;
import com.example.beacon.vdf.sources.SeedBuilder;
import com.example.beacon.vdf.sources.SeedSourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.beacon.vdf.infra.util.DateUtil.getCurrentTrucatedZonedDateTime;
import static java.lang.Thread.sleep;

@Service
public class CombinationService {

    private final Environment env;

    private List<SeedPostDto> seedList;

    private final ICipherSuite cipherSuite;

    private final SeedBuilder seedBuilder;

    private List<SeedUnicordCombinationVo> seedUnicordCombinationVos = new ArrayList<>();

    private final CombinationServiceCalcAndPersist combinationServiceCalcAndPersist;

    private static final Logger logger = LoggerFactory.getLogger(CombinationService.class);

    @Autowired
    public CombinationService(Environment env, SeedBuilder seedBuilder, CombinationServiceCalcAndPersist combinationServiceCalcAndPersist) {
        this.env = env;
        this.seedBuilder = seedBuilder;
        this.combinationServiceCalcAndPersist = combinationServiceCalcAndPersist;
        this.cipherSuite = CipherSuiteBuilder.build(0);
        this.seedList = new ArrayList<>();
    }

    public void run(String timeStamp) throws Exception {
        logger.warn("Start run:");

        List<SeedSourceDto> preDefSeedCombination = seedBuilder.getPreDefSeedCombination();

        // dalayed pulses?
        ZonedDateTime now = getCurrentTrucatedZonedDateTime();
        List<SeedSourceDto> delayedPulseList = preDefSeedCombination.stream()
                .filter(seedSourceDto ->
                        ZonedDateTime.parse(seedSourceDto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME)
                                .isBefore(now))
                .collect(Collectors.toList());

        if (!delayedPulseList.isEmpty()){
            final int countLimit = Integer.parseInt(env.getProperty("beacon.combination.sources.seconds-to-retry"));
            for (int i = 0; i < countLimit; i++) {
                List<SeedSourceDto> newList = getDelayedPulses();

                logger.warn("Combination - retry: {}" , i);

                List<SeedSourceDto> newListResult = newList.stream()
                        .filter(seedSourceDto ->
                                ZonedDateTime.parse(seedSourceDto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME)
                                        .isBefore(now))
                        .collect(Collectors.toList());

                if (newListResult.size() == 0){
                    preDefSeedCombination.clear();
                    preDefSeedCombination.addAll(newList);
                    break;
                }
            }

        }

        preDefSeedCombination
                .removeIf(seedSourceDto -> ZonedDateTime.parse(seedSourceDto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME)
                .isBefore(now));

        if (preDefSeedCombination.isEmpty()){
            return;
        }
        // dalayed pulses?

        List<SeedSourceDto> seeds = new ArrayList<>();
        seeds.addAll(preDefSeedCombination);
        seeds.addAll(seedBuilder.getHonestPartyCombination());

        seedUnicordCombinationVos = calcSeedConcat(seeds);

        final BigInteger x = new BigInteger(seedUnicordCombinationVos.get(seedUnicordCombinationVos.size() - 1).getCumulativeHash(), 16);

        seedList.clear();
        runAndPersist(x, timeStamp);
    }


    private List<SeedSourceDto> getDelayedPulses(){
        try {
            sleep(1000); // one second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return seedBuilder.getPreDefSeedCombination();
    }

    private List<SeedUnicordCombinationVo> calcSeedConcat(List<SeedSourceDto> seedList) {

        String currentValue = "";
        List<SeedUnicordCombinationVo> out = new ArrayList<>();

        for (SeedSourceDto dto : seedList) {
            currentValue = currentValue + dto.getSeed();
            String cumulativeDigest = cipherSuite.getDigest(currentValue);
            ZonedDateTime parse = ZonedDateTime.parse(dto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME);
            out.add(new SeedUnicordCombinationVo(dto.getUri(), dto.getSeed(), dto.getDescription(), cumulativeDigest, parse));
        }

        return out;
    }

    private void runAndPersist(BigInteger x, String timeStamp) throws Exception {
        combinationServiceCalcAndPersist.run(timeStamp, seedUnicordCombinationVos, x);
        logger.warn("Async...");
    }

}

