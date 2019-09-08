package com.example.beacon.vdf.sources;

import com.example.beacon.vdf.application.vdfpublic.SeedPostDto;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class SeedLocalRng implements SeedInterface {

    private final Environment env;

    private static final String DESCRIPTION = "Local RNG";

    @Autowired
    public SeedLocalRng(Environment env) {
        this.env = env;
    }

    @Override
    public SeedPostDto getSeed() {
        byte[] bytes = new byte[64];
        try {
            SecureRandom.getInstance(env.getProperty("vdf.seed.rng")).nextBytes(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new SeedPostDto(Hex.toHexString(bytes),DESCRIPTION);
    }
}
