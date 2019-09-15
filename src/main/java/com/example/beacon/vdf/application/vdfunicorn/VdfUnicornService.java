package com.example.beacon.vdf.application.vdfunicorn;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.VdfSeedDto;
import com.example.beacon.vdf.application.combination.StatusEnum;
import com.example.beacon.vdf.application.combination.VdfSerialize;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.infra.entity.VdfUnicornEntity;
import com.example.beacon.vdf.infra.entity.VdfUnicornSeedEntity;
import com.example.beacon.vdf.infra.util.DateUtil;
import com.example.beacon.vdf.repository.VdfUnicornRepository;
import com.example.beacon.vdf.sources.SeedBuilder;
import com.example.beacon.vdf.sources.SeedSourceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.example.beacon.vdf.infra.util.DateUtil.*;

@Service
public class VdfUnicornService {

    private final Environment env;

    private StatusEnum statusEnum;

    private ZonedDateTime timestamp;

//    private List<SeedPostDto> seedList;

    private List<SeedUnicordCombinationVo> seedListUnicordCombination;

    private final ICipherSuite cipherSuite;

    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

    private final SeedBuilder seedBuilder;

    private final VdfUnicornRepository vdfUnicornRepository;

    @Autowired
    public VdfUnicornService(Environment environment, SeedBuilder seedBuilder, VdfUnicornRepository vdfUnicornRepository) {
        this.env = environment;
        this.seedBuilder = seedBuilder;
        this.vdfUnicornRepository = vdfUnicornRepository;
        this.statusEnum = StatusEnum.STOPPED;
//        this.seedList = new ArrayList<>();
        this.seedListUnicordCombination = new ArrayList<>();
        this.cipherSuite = CipherSuiteBuilder.build(0);
        this.timestamp = getTimestampOfNextRun(ZonedDateTime.now());
    }

    public void startTimeSlot() {
//        this.seedList.clear();
        this.seedListUnicordCombination.clear();
        this.statusEnum = StatusEnum.OPEN;
        this.timestamp = getCurrentTrucatedZonedDateTime();

        List<SeedSourceDto> preDefinedSeeds = seedBuilder.getPreDefSeedUnicorn();
        preDefinedSeeds.forEach(dto -> {
            this.seedListUnicordCombination.add(
                    calcSeedConcat(new SeedPostDto(dto.getSeed(),
                            dto.getDescription(),
                            dto.getUri()),
                            this.seedListUnicordCombination,
                            timestamp));
        });
    }

    public void addSeed(SeedPostDto dto){
        SeedUnicordCombinationVo seedUnicordCombinationVo = calcSeedConcat(dto, this.seedListUnicordCombination,  ZonedDateTime.now());
        this.seedListUnicordCombination.add(seedUnicordCombinationVo);
    }

    private SeedUnicordCombinationVo calcSeedConcat(SeedPostDto dtoNew, List<SeedUnicordCombinationVo> seedList, ZonedDateTime now) {
        String currentValue = "";
        if (seedList.size() == 0) {
            currentValue = dtoNew.getSeed();
        } else {
            SeedUnicordCombinationVo lastSeed = seedList.get(seedList.size() - 1);
            currentValue = lastSeed + dtoNew.getSeed();
        }
        String cumulativeDigest = cipherSuite.getDigest(currentValue);
        return new SeedUnicordCombinationVo(dtoNew.getUri(), dtoNew.getSeed(), dtoNew.getDescription(), cumulativeDigest, now);
    }

    public void endTimeSlot() throws Exception {

        if (this.seedListUnicordCombination.isEmpty()){
            this.statusEnum = StatusEnum.STOPPED;
            return;
        }

        this.statusEnum = StatusEnum.RUNNING;
        List<SeedSourceDto> honestSeeds = seedBuilder.getHonestPartyUnicorn();

        honestSeeds.forEach(dto -> {
            this.seedListUnicordCombination.add(
                    calcSeedConcat(new SeedPostDto(dto.getSeed(),
                                    dto.getDescription(),
                                    dto.getUri()),
                            this.seedListUnicordCombination,
                            ZonedDateTime.now()));
        });
        run();
    }

    public boolean isOpen(){
        return this.statusEnum.equals(StatusEnum.OPEN);
    }

    private void run() throws Exception {
        SeedUnicordCombinationVo last = this.seedListUnicordCombination.get(this.seedListUnicordCombination.size() - 1);
        final BigInteger x = new BigInteger(last.getCumulativeHash(), 16);

        int iterations = Integer.parseInt(env.getProperty("beacon.unicorn.iterations"));

        BigInteger y = VdfSloth.mod_op(x, iterations);

        persist(y,x, iterations);
        seedListUnicordCombination.clear();
        this.statusEnum = StatusEnum.STOPPED;
        this.timestamp = getTimestampOfNextRun(ZonedDateTime.now());
    }

    public UnicornCurrentDto getUnicornState(){
        UnicornCurrentDto unicornCurrentDto = new UnicornCurrentDto();
        unicornCurrentDto.setStatusEnum(this.statusEnum.toString());

        if (!this.seedListUnicordCombination.isEmpty()){
            unicornCurrentDto.setCurrentHash(this.seedListUnicordCombination.get(this.seedListUnicordCombination.size() - 1).getCumulativeHash());
        }

        //TODO Verificar o horário
        unicornCurrentDto.setStart(getTimeStampFormated(this.timestamp));

        ZonedDateTime nextRun = getTimestampOfNextRun(ZonedDateTime.now());
        long minutesForNextRun = DateUtil.getMinutesForNextRun(ZonedDateTime.now(), nextRun);
        unicornCurrentDto.setNextRunInMinutes(minutesForNextRun);

        DateUtil.getTimestampOfNextRun(ZonedDateTime.now()).plus(15, ChronoUnit.MINUTES);
        unicornCurrentDto.setEnd(getTimeStampFormated(DateUtil.getTimestampOfNextRun(ZonedDateTime.now()).plus(15, ChronoUnit.MINUTES)));

        this.seedListUnicordCombination.forEach(s ->
                unicornCurrentDto.addSeed(new VdfSeedDto(s.getSeed(),
                        DateUtil.getTimeStampFormated(s.getTimeStamp()),
                        s.getDescription(), s.getUri(),
                        s.getCumulativeHash())));

        return unicornCurrentDto;
    }

    @Transactional
    protected void persist(BigInteger y, BigInteger x, int iterations) throws Exception {

        Long maxPulseIndex = vdfUnicornRepository.findMaxId();

        if (maxPulseIndex==null){
            maxPulseIndex = 1L;
        } else {
            maxPulseIndex = maxPulseIndex + 1L ;
        }

        String uri = env.getProperty("beacon.url") +  "/beacon/vdf/unicorn/pulse/" + maxPulseIndex;

        VdfUnicornEntity unicornEntity = new VdfUnicornEntity();
        unicornEntity.setUri(uri);
        unicornEntity.setVersion("1.0");
        unicornEntity.setPulseIndex(maxPulseIndex);
        unicornEntity.setTimeStamp(this.timestamp.truncatedTo(ChronoUnit.MINUTES));
        unicornEntity.setCertificateId(this.certificateId);
        unicornEntity.setCipherSuite(0);
        unicornEntity.setCombination(env.getProperty("vdf.combination").toUpperCase());
        unicornEntity.setPeriod(Integer.parseInt(env.getProperty("beacon.unicorn.period")));

        unicornEntity.setP("9325099249067051137110237972241325094526304716592954055103859972916682236180445434121127711536890366634971622095209473411013065021251467835799907856202363");
        unicornEntity.setX(x.toString());
        unicornEntity.setY(y.toString());
        unicornEntity.setIterations(iterations);

        this.seedListUnicordCombination.forEach(SeedUnicordCombinationVo ->
                unicornEntity.addSeed(new VdfUnicornSeedEntity(SeedUnicordCombinationVo, unicornEntity)));

        signPulse(VdfSerialize.serializeVdfPublic(unicornEntity), unicornEntity);
        vdfUnicornRepository.saveAndFlush(unicornEntity);

    }

    private void signPulse(byte[] bytes, VdfUnicornEntity vdfPulseEntity) throws Exception {
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        vdfPulseEntity.setSignatureValue(cipherSuite.sign(privateKey, bytes));
    }

}
