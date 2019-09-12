package com.example.beacon.vdf.application.vdfunicorn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UnicornCurrentDtoUnicornServiceTest {

    @Autowired
    VdfUnicornService vdfUnicornService;

    @Test
    public void startTimeSlot() throws Exception {
        vdfUnicornService.startTimeSlot();
        vdfUnicornService.addSeed(new SeedPostDto("abc", "minha contribuicao", "http://virus.com"));
        vdfUnicornService.endTimeSlot();
    }
}