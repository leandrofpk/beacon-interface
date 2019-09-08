package com.example.beacon.vdf.sources;

import com.example.beacon.vdf.application.vdfpublic.SeedPostDto;
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

    private final List<SeedPostDto> seedList = new ArrayList<>();

    @Autowired
    public SeedBuilder(ApplicationContext context, Environment env) {
        this.context = context;
        this.env = env;
    }

    public List<SeedPostDto> getPreDefSeed(){
        seedList.clear();
        seedList.add(context.getBean(SeedLocalPrecommitment.class).getSeed());
        seedList.add(context.getBean(SeedLastNist.class).getSeed());
        seedList.add(context.getBean(SeedLastChile.class).getSeed());
        seedList.add(context.getBean(SeedLocalRng.class).getSeed());
        return Collections.unmodifiableList(seedList);
    }

    public List<SeedPostDto> getHonestParty(){
        seedList.clear();
        seedList.add(context.getBean(SeedLastNist.class).getSeed());
        seedList.add(context.getBean(SeedLastChile.class).getSeed());
        seedList.add(context.getBean(SeedLocalRng.class).getSeed());
        seedList.add(context.getBean(SeedLocalPrecommitment.class).getSeed());
        return Collections.unmodifiableList(seedList);
    }
}
