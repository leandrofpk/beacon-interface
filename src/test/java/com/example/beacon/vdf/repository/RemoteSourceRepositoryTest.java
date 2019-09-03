package com.example.beacon.vdf.repository;

import com.example.beacon.interfac.api.dto.PulseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteSourceRepositoryTest {

    @Test
    public void get() {
        RestTemplate restTemplate = new RestTemplate();
        PulseDto lastPulse = restTemplate.getForObject("https://beacon.nist.gov/beacon/2.0/pulse/last", PulseDto.class);
        PulseDto lastPulse2 = restTemplate.getForObject("https://random.uchile.cl/beacon/2.0/pulse/last", PulseDto.class);

        System.out.println(lastPulse);



    }
}