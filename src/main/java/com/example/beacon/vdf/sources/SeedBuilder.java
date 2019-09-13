package com.example.beacon.vdf.sources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SeedBuilder {

    private final ApplicationContext context;

    private final Environment env;

    private final List<SeedSourceDto> seedList = new ArrayList<>();

    @Autowired
    public SeedBuilder(ApplicationContext context, Environment env) {
        this.context = context;
        this.env = env;
    }

    // Beacon Combination
    public List<SeedSourceDto> getPreDefSeedCombination(){
        seedList.clear();
        seedList.add(context.getBean(SeedLastNist.class).getSeed());
        seedList.add(context.getBean(SeedLastChile.class).getSeed());
        return Collections.unmodifiableList(new ArrayList<>(seedList));
    }

    public List<SeedSourceDto> getHonestPartyCombination(){
        seedList.clear();
        seedList.add(context.getBean(SeedLocalPrecommitment.class).getSeed());
        return Collections.unmodifiableList(new ArrayList<>(seedList));
    }

    // VDF / Unicorn
    public List<SeedSourceDto> getPreDefSeedUnicorn(){
        seedList.clear();
        return Collections.unmodifiableList(new ArrayList<>(seedList));
    }

    public List<SeedSourceDto> getHonestPartyUnicorn(){
        seedList.clear();
        seedList.add(context.getBean(SeedLocalPrecommitmentUnicorn.class).getSeed());
        return Collections.unmodifiableList(new ArrayList<>(seedList));
    }
}
