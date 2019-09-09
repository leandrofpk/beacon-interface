package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.vdfpublic.SeedPostDto;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import com.example.beacon.vdf.infra.entity.VdfSeedEntity;
import com.example.beacon.vdf.repository.VdfPulsesRepository;
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

@Service
public class VdfPulseService {

    private final Environment env;

    private StatusEnum statusEnum;

    private List<SeedPostDto> seedList;

    private final VdfPulsesRepository vdfPulsesRepository;

    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

    private ZonedDateTime timestamp;

    private final ICipherSuite cipherSuite;

    private final SeedBuilder seedBuilder;

    private byte[] currentXorValue = new byte[128];

    private String currentConcatValue;

    private String currentHash;

    @Autowired
    public VdfPulseService(Environment env, VdfPulsesRepository vdfPulsesRepository, SeedBuilder seedBuilder) {
        this.env = env;
        this.vdfPulsesRepository = vdfPulsesRepository;
        this.seedBuilder = seedBuilder;
        this.statusEnum = StatusEnum.STOPPED;
        this.cipherSuite = CipherSuiteBuilder.build(0);

        this.seedList = new ArrayList<>();
    }

    public void startTimeSlot(){
        this.seedList.clear();
        this.statusEnum = StatusEnum.OPEN;
        this.timestamp = getCurrentTrucatedZonedDateTime();
        seedBuilder.getPreDefSeed().forEach(dto -> addSeed(dto));
    }

    public void addSeed(SeedPostDto dto){
        this.seedList.add(dto);
        calcSeed(dto);
    }

    public void endTimeSlot() throws Exception {
        this.statusEnum = StatusEnum.RUNNING;
        seedBuilder.getHonestParty()
                .forEach(dto -> calcSeed(dto));
        run();
    }

    public boolean isOpen(){
        return this.statusEnum.equals(StatusEnum.OPEN);
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

    private void run() throws Exception {
        final BigInteger x = new BigInteger(this.currentHash, 16);
        int iterations = Integer.parseInt(env.getProperty("vdf.public.iterations"));

        BigInteger y = VdfSloth.mod_op(x, iterations);

        persist(y,x, iterations);
        seedList.clear();
        this.statusEnum = StatusEnum.STOPPED;
    }

    @Transactional
    public void persist(BigInteger y, BigInteger x, int iterations) throws Exception {
        Long maxPulseIndex = vdfPulsesRepository.findMaxId();

        if (maxPulseIndex==null){
            maxPulseIndex = 1L;
        } else {
            maxPulseIndex = maxPulseIndex + 1L ;
        }

        VdfPulseEntity vdfPulseEntity = new VdfPulseEntity();
        vdfPulseEntity.setPulseIndex(maxPulseIndex);
        vdfPulseEntity.setTimeStamp(this.timestamp);
        vdfPulseEntity.setCertificateId(this.certificateId);
        vdfPulseEntity.setCipherSuite(0);
        vdfPulseEntity.setPeriod(Integer.parseInt(env.getProperty("vdf.pulse.period")));

        vdfPulseEntity.setX(x.toString());
        vdfPulseEntity.setY(y.toString());
        vdfPulseEntity.setIterations(iterations);

        seedList.forEach(vdfPulseDto ->
                vdfPulseEntity.addSeed(new VdfSeedEntity(vdfPulseDto, vdfPulseEntity)));

        signPulse(VdfSerialize.serializeVdfEntity(vdfPulseEntity), vdfPulseEntity);
        vdfPulsesRepository.saveAndFlush(vdfPulseEntity);
    }

    private void signPulse(byte[] bytes, VdfPulseEntity vdfPulseEntity) throws Exception {
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        vdfPulseEntity.setSignatureValue(cipherSuite.sign(privateKey, bytes));
    }

}

