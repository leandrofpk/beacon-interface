package com.example.beacon.vdf.application.combination;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.application.vdfunicorn.SeedPostDto;
import com.example.beacon.vdf.infra.entity.CombinationEntity;
import com.example.beacon.vdf.infra.entity.CombinationSeedEntity;
import com.example.beacon.vdf.repository.CombinationRepository;
import com.example.beacon.vdf.scheduling.VdfQueueConsumer;
import com.example.beacon.vdf.sources.SeedBuilder;
import com.example.beacon.vdf.sources.SeedSourceDto;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.beacon.vdf.infra.util.DateUtil.getCurrentTrucatedZonedDateTime;

@Service
public class CombinationService {

    private final Environment env;

    private List<SeedPostDto> seedList;

    private final CombinationRepository combinationRepository;

    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

    private final ICipherSuite cipherSuite;

    private final SeedBuilder seedBuilder;

    private List<SeedUnicordCombinationVo> seedUnicordCombinationVos = new ArrayList<>();

    @Autowired
    public CombinationService(Environment env, CombinationRepository combinationRepository, SeedBuilder seedBuilder) {
        this.env = env;
        this.combinationRepository = combinationRepository;
        this.seedBuilder = seedBuilder;
        this.cipherSuite = CipherSuiteBuilder.build(0);

        this.seedList = new ArrayList<>();
    }

    public void run(String timeStamp) throws Exception {
        List<SeedSourceDto> preDefSeedCombination = seedBuilder.getPreDefSeedCombination();
        List<SeedSourceDto> honestPartyCombination = seedBuilder.getHonestPartyCombination();

        List<SeedSourceDto> seeds = new ArrayList<>();
        seeds.addAll(preDefSeedCombination);
        seeds.addAll(honestPartyCombination);

        seedUnicordCombinationVos = calcSeedConcat(seeds);

        final BigInteger x = new BigInteger(seedUnicordCombinationVos.get(seedUnicordCombinationVos.size() - 1).getCumulativeHash(), 16);
        int iterations = Integer.parseInt(env.getProperty("beacon.combination.iterations"));

        BigInteger y = VdfSloth.mod_op(x, iterations);

        persist(y,x, iterations, timeStamp);
        seedList.clear();
    }

    private List<SeedUnicordCombinationVo> calcSeedConcat(List<SeedSourceDto> seedList) {

        String currentValue = "";
        List<SeedUnicordCombinationVo> out = new ArrayList<>();

        for (SeedSourceDto dto : seedList) {
            currentValue = currentValue + dto.getSeed();
            String cumulativeDigest = cipherSuite.getDigest(currentValue);
            out.add(new SeedUnicordCombinationVo(dto.getUri(), dto.getSeed(), dto.getDescription(), cumulativeDigest, ZonedDateTime.now()));
        }

        return out;
    }

    @Transactional
    protected void persist(BigInteger y, BigInteger x, int iterations, String timeStamp) throws Exception {
        Long maxPulseIndex = combinationRepository.findMaxId();

        if (maxPulseIndex==null){
            maxPulseIndex = 1L;
        } else {
            maxPulseIndex = maxPulseIndex + 1L ;
        }

        CombinationEntity combinationEntity = new CombinationEntity();
        combinationEntity.setPulseIndex(maxPulseIndex);
        combinationEntity.setTimeStamp(ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME));
        combinationEntity.setCertificateId(this.certificateId);
        combinationEntity.setCipherSuite(0);
        combinationEntity.setCombination(env.getProperty("vdf.combination").toUpperCase());
        combinationEntity.setPeriod(Integer.parseInt(env.getProperty("beacon.combination.period")));

        combinationEntity.setP("9325099249067051137110237972241325094526304716592954055103859972916682236180445434121127711536890366634971622095209473411013065021251467835799907856202363");
        combinationEntity.setX(x.toString());
        combinationEntity.setY(y.toString());
        combinationEntity.setIterations(iterations);

        seedUnicordCombinationVos.forEach(dto ->
                combinationEntity.addSeed(new CombinationSeedEntity(dto, combinationEntity)));

        signPulse(VdfSerialize.serializeCombinationEntity(combinationEntity), combinationEntity);
        combinationRepository.saveAndFlush(combinationEntity);
    }

    private void signPulse(byte[] bytes, CombinationEntity vdfPulseEntity) throws Exception {
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        vdfPulseEntity.setSignatureValue(cipherSuite.sign(privateKey, bytes));
    }

}

