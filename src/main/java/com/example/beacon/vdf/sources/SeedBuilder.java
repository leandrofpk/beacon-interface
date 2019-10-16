package com.example.beacon.vdf.sources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.beacon.vdf.infra.util.DateUtil.getCurrentTrucatedZonedDateTime;

@Component
public class SeedBuilder {

    private final ApplicationContext context;

    private final Environment env;

    @Autowired
    public SeedBuilder(ApplicationContext context, Environment env) {
        this.context = context;
        this.env = env;
    }

    // Beacon Combination
//    public List<SeedSourceDto> getPreDefSeedCombination(){
//        final List<SeedSourceDto> seedList = new ArrayList<>();
//        return Collections.unmodifiableList(new ArrayList<>(seedList));
//    }

    public List<SeedSourceDto> getHonestPartyCombination(){
        final List<SeedSourceDto> seedList = new ArrayList<>();
        seedList.add(context.getBean(SeedLastNist.class).getSeed());
        seedList.add(context.getBean(SeedLastChile.class).getSeed());

        calc(seedList);

        seedList.add(context.getBean(SeedLocalPrecommitment.class).getSeed());
        return Collections.unmodifiableList(new ArrayList<>(seedList));
    }

    // VDF / Unicorn
    public List<SeedSourceDto> getPreDefSeedUnicorn(){
        final List<SeedSourceDto> seedList = new ArrayList<>();
        // do something
        return Collections.unmodifiableList(new ArrayList<>(seedList));
    }

    public List<SeedSourceDto> getHonestPartyUnicorn(){
        final List<SeedSourceDto> seedList = new ArrayList<>();
        seedList.add(context.getBean(SeedLocalPrecommitmentUnicorn.class).getSeed());
        return Collections.unmodifiableList(new ArrayList<>(seedList));
    }

    private void calc(List<SeedSourceDto> seedList) throws InterruptedException {
        ZonedDateTime now = getCurrentTrucatedZonedDateTime();

        List<SeedSourceDto> collect = seedList.stream()
                .filter(seedSourceDto ->
                        ZonedDateTime.parse(seedSourceDto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME).isBefore(now))
                .collect(Collectors.toList());

        final int countLimit = 7;
        if (collect.isEmpty()){
            return;
        } else {
            // buscar novamente até 7 segundos

            int i = 1;
            for (SeedSourceDto seed: collect) {
                Thread.sleep(1000);
                SeedSourceDto bean = context.getBean(seed.getClass());
                System.out.println(bean);

                if (bean.getTimeStamp())

                if (i <= countLimit){

                }
                i++;
            }
        }


    }

}
