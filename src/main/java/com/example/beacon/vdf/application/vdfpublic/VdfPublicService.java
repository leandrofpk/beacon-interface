package com.example.beacon.vdf.application.vdfpublic;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.SubmissionTime;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.vdfbeacon.CombinatioEnum;
import com.example.beacon.vdf.application.vdfbeacon.StatusEnum;
import com.example.beacon.vdf.application.vdfbeacon.VdfSerialize;
import com.example.beacon.vdf.infra.entity.VdfPublicEntity;
import com.example.beacon.vdf.infra.entity.VdfSeedPublicEntity;
import com.example.beacon.vdf.infra.util.DateUtil;
import com.example.beacon.vdf.repository.VdfPulsesPublicRepository;
import com.example.beacon.vdf.sources.SeedBuilder;
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
import static com.example.beacon.vdf.infra.util.DateUtil.getTimestampOfNextRun;

@Service
public class VdfPublicService {

    private final Environment env;

    private StatusEnum statusEnum;

    private ZonedDateTime timestamp;

    private List<SeedPostDto> seedList;

    private final ICipherSuite cipherSuite;

    private String currentHash;

    private byte[] currentXorValue = new byte[128];

    private String currentConcatValue;

    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

    private final SeedBuilder seedBuilder;

    private final VdfPulsesPublicRepository vdfPulsesPublicRepository;

    @Autowired
    public VdfPublicService(Environment environment, SeedBuilder seedBuilder, VdfPulsesPublicRepository vdfPulsesPublicRepository) {
        this.env = environment;
        this.seedBuilder = seedBuilder;
        this.vdfPulsesPublicRepository = vdfPulsesPublicRepository;
        this.statusEnum = StatusEnum.STOPPED;
        this.seedList = new ArrayList<>();
        this.cipherSuite = CipherSuiteBuilder.build(0);
        this.timestamp = getTimestampOfNextRun(ZonedDateTime.now());
    }

    public void startTimeSlot() {
        this.seedList.clear();
        this.statusEnum = StatusEnum.OPEN;
        this.timestamp = getCurrentTrucatedZonedDateTime();

        List<SeedPostDto> preDefinedSeeds = seedBuilder.getPreDefSeed();
        preDefinedSeeds.forEach(dto -> calcSeed(dto));
    }

    public void addSeed(SeedPostDto dto){
        dto.setSeed(cipherSuite.getDigest(dto.getSeed()));
        this.seedList.add(dto);
        calcSeed(dto);
    }

    public void endTimeSlot() throws Exception {
        this.statusEnum = StatusEnum.RUNNING;

        List<SeedPostDto> honestSeeds = seedBuilder.getHonestParty();
        honestSeeds.forEach(dto -> calcSeed(dto));

        run();
    }

    public boolean isOpen(){
        return this.statusEnum.equals(StatusEnum.OPEN);
    }

    private void run() throws Exception {
        final BigInteger x = new BigInteger(this.currentHash, 16);
        int iterations = Integer.parseInt(env.getProperty("vdf.public.iterations"));


        BigInteger y = VdfSloth.mod_op(x, iterations);

        persist(y,x, iterations);
        seedList.clear();
        this.statusEnum = StatusEnum.STOPPED;
        this.timestamp = getTimestampOfNextRun(ZonedDateTime.now());
    }

    public Vdf getCurrentVdf(){
        Vdf vdf = new Vdf();
        vdf.setStatusEnum(this.statusEnum.getDescription());
        vdf.setCurrentHash(this.currentHash);
        vdf.setSubmissionTime(new SubmissionTime(this.timestamp, 15));
        return vdf;
    }

    private void calcSeed(SeedPostDto dto) {

        CombinatioEnum combination = CombinatioEnum.valueOf(env.getProperty("vdf.combination").toUpperCase());

        if (seedList.size() == 0) {
            if (combination.equals(CombinatioEnum.XOR)) {
                this.currentXorValue = dto.getSeed().getBytes(StandardCharsets.UTF_8);
            } else {
                this.currentConcatValue = dto.getSeed();
            }
        } else {
            if (combination.equals(CombinatioEnum.XOR)) {
                this.currentXorValue = ByteUtils.xor(this.currentXorValue, dto.getSeed().getBytes(StandardCharsets.UTF_8));
            } else {
                this.currentConcatValue = currentConcatValue + dto.getSeed();
            }
        }

        if (combination.equals(CombinatioEnum.XOR)) {
            this.currentHash = cipherSuite.getDigest(currentXorValue);
        } else {
            this.currentHash = cipherSuite.getDigest(currentConcatValue);
        }

        this.seedList.add(dto);

    }

    @Transactional
    protected void persist(BigInteger y, BigInteger x, int iterations) throws Exception {

        Long maxPulseIndex = vdfPulsesPublicRepository.findMaxId();

        if (maxPulseIndex==null){
            maxPulseIndex = 1L;
        } else {
            maxPulseIndex = maxPulseIndex + 1L ;
        }

        VdfPublicEntity vdfPublicEntity = new VdfPublicEntity();
        vdfPublicEntity.setPulseIndex(maxPulseIndex);
        vdfPublicEntity.setTimeStamp(this.timestamp);
        vdfPublicEntity.setCertificateId(this.certificateId);
        vdfPublicEntity.setCipherSuite(0);
        vdfPublicEntity.setPeriod(Integer.parseInt(env.getProperty("vdf.public.period")));

        vdfPublicEntity.setX(x.toString());
        vdfPublicEntity.setY(y.toString());
        vdfPublicEntity.setIterations(iterations);

        seedList.forEach(seedPostDto ->
                vdfPublicEntity.addSeed(new VdfSeedPublicEntity(seedPostDto, vdfPublicEntity)));

        signPulse(VdfSerialize.serializeVdfPublic(vdfPublicEntity), vdfPublicEntity);
        vdfPulsesPublicRepository.saveAndFlush(vdfPublicEntity);

    }

    private void signPulse(byte[] bytes, VdfPublicEntity vdfPulseEntity) throws Exception {
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        vdfPulseEntity.setSignatureValue(cipherSuite.sign(privateKey, bytes));
    }

}
