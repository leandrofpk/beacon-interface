package com.example.beacon.vdf.application.combination;

import com.example.beacon.shared.ByteSerializationFields;
import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.application.vdfunicorn.SeedPostDto;
import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import com.example.beacon.vdf.infra.entity.CombinationEntity;
import com.example.beacon.vdf.infra.entity.CombinationSeedEntity;
import com.example.beacon.vdf.repository.CombinationRepository;
import com.example.beacon.vdf.scheduling.CombinationResultDto;
import com.example.beacon.vdf.sources.SeedBuilder;
import com.example.beacon.vdf.sources.SeedCombinationResult;
import com.example.beacon.vdf.sources.SeedSourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
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

    private final CombinationRepository combinationRepository;

    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

    private final ICipherSuite cipherSuite;

    private final SeedBuilder seedBuilder;

    private final VdfUnicornService vdfUnicornService;

    private List<SeedUnicordCombinationVo> seedUnicordCombinationVos = new ArrayList<>();

    private final SeedCombinationResult seedCombinationResult;

    private static final Logger logger = LoggerFactory.getLogger(CombinationService.class);

    @Autowired
    public CombinationService(Environment env, CombinationRepository combinationRepository, SeedBuilder seedBuilder, VdfUnicornService vdfUnicornService, SeedCombinationResult seedCombinationResult) {
        this.env = env;
        this.combinationRepository = combinationRepository;
        this.seedBuilder = seedBuilder;
        this.vdfUnicornService = vdfUnicornService;
        this.seedCombinationResult = seedCombinationResult;
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
        int iterations = Integer.parseInt(env.getProperty("beacon.combination.iterations"));

        logger.warn("Start combination sloth:");
        BigInteger y = VdfSloth.mod_op(x, iterations);
        logger.warn("End combination sloth:");

        persist(y,x, iterations, timeStamp);
        seedList.clear();
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
//            logger.warn("Current value: {}", currentValue);
            String cumulativeDigest = cipherSuite.getDigest(currentValue);
//            logger.warn("cumulativeDigest: {}", cumulativeDigest);
            ZonedDateTime parse = ZonedDateTime.parse(dto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME);
            out.add(new SeedUnicordCombinationVo(dto.getUri(), dto.getSeed(), dto.getDescription(), cumulativeDigest, parse));
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

        String uri = env.getProperty("beacon.url") +  "/beacon/2.0/combination/pulse/" + maxPulseIndex;

        CombinationEntity combinationEntity = new CombinationEntity();
        combinationEntity.setUri(uri);
        combinationEntity.setVersion("Version 1.0");
        combinationEntity.setPulseIndex(maxPulseIndex);
        combinationEntity.setTimeStamp(ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME));
        combinationEntity.setCertificateId(this.certificateId);
        combinationEntity.setCipherSuite(0);
        combinationEntity.setCombination(env.getProperty("vdf.combination").toUpperCase());
        combinationEntity.setPeriod(Integer.parseInt(env.getProperty("beacon.combination.period")));

        seedUnicordCombinationVos.forEach(dto ->
                combinationEntity.addSeed(new CombinationSeedEntity(dto, combinationEntity)));

        combinationEntity.setP("9325099249067051137110237972241325094526304716592954055103859972916682236180445434121127711536890366634971622095209473411013065021251467835799907856202363");
        combinationEntity.setX(x.toString());
        combinationEntity.setY(y.toString());
        combinationEntity.setIterations(iterations);

        //sign
        ByteSerializationFields serialization = new ByteSerializationFields(combinationEntity);
        ByteArrayOutputStream baos = serialization.getBaos();

        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        String signature = cipherSuite.signPkcs15(privateKey, serialization.getBaos().toByteArray());
        combinationEntity.setSignatureValue(signature);

        //outputvalue
        baos.write(serialization.byteSerializeSig(signature).toByteArray());
        String output = cipherSuite.getDigest(baos.toByteArray());
        combinationEntity.setOutputValue(output);

        combinationRepository.saveAndFlush(combinationEntity);

        CombinationResultDto combinationResultDto = new CombinationResultDto(combinationEntity.getTimeStamp().toString(), combinationEntity.getOutputValue(), combinationEntity.getUri());
        sendToUnicorn(combinationResultDto);

        logger.warn("Stop run:");
    }

    @Async
    protected void sendToUnicorn(CombinationResultDto combinationResultDto) throws Exception {
//        ZonedDateTime parse = ZonedDateTime.parse(dto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME);
//        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
//
//        long between = ChronoUnit.MINUTES.between(parse, now);

        if (!vdfUnicornService.isOpen()){
            return;
        }

        logger.warn(combinationResultDto.toString());
        seedCombinationResult.setCombinationResultDto(combinationResultDto);
        vdfUnicornService.endTimeSlot();
    }

}

