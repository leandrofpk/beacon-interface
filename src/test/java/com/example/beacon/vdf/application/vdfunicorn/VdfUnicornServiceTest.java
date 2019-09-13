package com.example.beacon.vdf.application.vdfunicorn;

import com.example.beacon.vdf.sources.SeedLocalPrecommitmentUnicorn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VdfUnicornServiceTest {

    @Autowired
    VdfUnicornService vdfUnicornService;

    @Autowired
    SeedLocalPrecommitmentUnicorn seedLocalPrecommitmentUnicorn;

    @Test
    public void test() throws Exception {
        vdfUnicornService.startTimeSlot();
        SeedPostDto seedPostDto = new SeedPostDto("meu seed", "description de teste", "uri de teste");
        vdfUnicornService.addSeed(seedPostDto);
        seedLocalPrecommitmentUnicorn.setPrecommitment("a");
        vdfUnicornService.endTimeSlot();
    }

}