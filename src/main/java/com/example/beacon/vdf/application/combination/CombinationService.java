package com.example.beacon.vdf.application.combination;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.application.vdfunicorn.SeedPostDto;
import com.example.beacon.vdf.infra.entity.CombinationEntity;
import com.example.beacon.vdf.repository.CombinationRepository;
import com.example.beacon.vdf.sources.SeedBuilder;
import com.example.beacon.vdf.sources.SeedSourceDto;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.beacon.vdf.infra.util.DateUtil.getCurrentTrucatedZonedDateTime;

@Service
public class CombinationService {

    private final Environment env;

//    private StatusEnum statusEnum;

    private List<SeedPostDto> seedList;

    private final CombinationRepository combinationRepository;

    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

    private ZonedDateTime timestamp;

    private final ICipherSuite cipherSuite;

    private final SeedBuilder seedBuilder;

    private byte[] currentXorValue = new byte[128];

    private String currentConcatValue;

//    private String currentHash;

    @Autowired
    public CombinationService(Environment env, CombinationRepository combinationRepository, SeedBuilder seedBuilder) {
        this.env = env;
        this.combinationRepository = combinationRepository;
        this.seedBuilder = seedBuilder;
//        this.statusEnum = StatusEnum.STOPPED;
        this.cipherSuite = CipherSuiteBuilder.build(0);

        this.seedList = new ArrayList<>();
    }

//    private void calcSeed(SeedPostDto dto) {
//
//        CombinationEnum combination = CombinationEnum.valueOf(env.getProperty("vdf.combination").toUpperCase());
//
//        if (seedList.size() == 0) {
//            if (combination.equals(CombinationEnum.XOR)) {
//                this.currentXorValue = dto.getSeed().getBytes(StandardCharsets.UTF_8);
//            } else {
//                this.currentConcatValue = dto.getSeed();
//            }
//        } else {
//            if (combination.equals(CombinationEnum.XOR)) {
//                this.currentXorValue = ByteUtils.xor(this.currentXorValue, dto.getSeed().getBytes(StandardCharsets.UTF_8));
//            } else {
//                this.currentConcatValue = currentConcatValue + dto.getSeed();
//            }
//        }
//
//        if (combination.equals(CombinationEnum.XOR)) {
//            this.currentHash = cipherSuite.getDigest(currentXorValue);
//        } else {
//            this.currentHash = cipherSuite.getDigest(currentConcatValue);
//        }
//
//        this.seedList.add(dto);
//
//    }

    public void run() throws Exception {
        List<SeedSourceDto> seeds = seedBuilder.getPreDefSeedCombination();
        seeds.addAll(seedBuilder.getHonestPartyCombination());
        List<SeedUnicordCombinationVo> seedUnicordCombinationVos = calcSeedConcat(seeds);

        final BigInteger x = new BigInteger(seedUnicordCombinationVos.get(seedUnicordCombinationVos.size() - 1).getCumulativeHash(), 16);
        int iterations = Integer.parseInt(env.getProperty("beacon.combination.iterations"));

        BigInteger y = VdfSloth.mod_op(x, iterations);

        persist(y,x, iterations);
        seedList.clear();
//        this.statusEnum = StatusEnum.STOPPED;
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

//        if (seedList.size() == 0) {
//            currentValue = dtoNew.getSeed();
//        } else {
//            SeedUnicordCombinationVo lastSeed = seedList.get(seedList.size() - 1);
//            currentValue = lastSeed + dtoNew.getSeed();
//        }
//        String cumulativeDigest = cipherSuite.getDigest(currentValue);
//        return new SeedUnicordCombinationVo(dtoNew.getUri(), dtoNew.getSeed(), dtoNew.getDescription(), cumulativeDigest, now);
    }

    @Transactional
    protected void persist(BigInteger y, BigInteger x, int iterations) throws Exception {
        Long maxPulseIndex = combinationRepository.findMaxId();

        if (maxPulseIndex==null){
            maxPulseIndex = 1L;
        } else {
            maxPulseIndex = maxPulseIndex + 1L ;
        }

        CombinationEntity vdfPulseEntity = new CombinationEntity();
        vdfPulseEntity.setPulseIndex(maxPulseIndex);
        vdfPulseEntity.setTimeStamp(this.timestamp);
        vdfPulseEntity.setCertificateId(this.certificateId);
        vdfPulseEntity.setCipherSuite(0);
        vdfPulseEntity.setPeriod(Integer.parseInt(env.getProperty("beacon.combination.period")));

        vdfPulseEntity.setX(x.toString());
        vdfPulseEntity.setY(y.toString());
        vdfPulseEntity.setIterations(iterations);

//        seedList.forEach(vdfPulseDto ->
//                vdfPulseEntity.addSeed(new VdfUnicornSeedEntity(vdfPulseDto, vdfPulseEntity)));

        signPulse(VdfSerialize.serializeCombinationEntity(vdfPulseEntity), vdfPulseEntity);
        combinationRepository.saveAndFlush(vdfPulseEntity);
    }

    private void signPulse(byte[] bytes, CombinationEntity vdfPulseEntity) throws Exception {
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        vdfPulseEntity.setSignatureValue(cipherSuite.sign(privateKey, bytes));
    }

}

