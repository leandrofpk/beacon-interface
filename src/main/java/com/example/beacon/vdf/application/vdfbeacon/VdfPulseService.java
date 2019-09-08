package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.vdfbeacon.dto.VdfPulseDtoPost;
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

    private List<VdfPulseDtoPost> seedList;

    private final VdfPulsesRepository vdfPulsesRepository;

    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

    private ZonedDateTime timestamp;

    private String precommitment = "";

    private final ICipherSuite cipherSuite;

    private final SeedBuilder seedBuilder;

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
        this.statusEnum = StatusEnum.OPEN;
        this.timestamp = getCurrentTrucatedZonedDateTime();

        List<SeedPostDto> preDefSeed = seedBuilder.getPreDefSeed();

        for (SeedPostDto dto : preDefSeed) {
            VdfPulseDtoPost vdfPulseDtoPost = new VdfPulseDtoPost();
            vdfPulseDtoPost.setOriginEnum(OriginEnum.INMETRO);
            vdfPulseDtoPost.setSeed(dto.getSeed());
        }
    }

    public void addSeed(VdfPulseDtoPost dto){
        this.seedList.add(dto);
    }

    public void endTimeSlot() throws Exception {
        this.statusEnum = StatusEnum.RUNNING;
        run();
    }

    public boolean isOpen(){
        return this.statusEnum.equals(StatusEnum.OPEN);
    }

    private void run() throws Exception {
//        ZonedDateTime timeStamp = entropyRepository.findNewerNumber();
//
//        long between = ChronoUnit.MINUTES.between(timeStamp, ZonedDateTime.now());
//
//        if (between > 2){
////            this.precommitment = "0EE710ADDAFA8774268E736A92B65C82901087BD926886147179BAD110ADCA5EFDE28099C94DEC2EE2A328369A72737C564C6C3DA08CE7057DC9B7B1D02BFBB2";
//            this.precommitment = getNoise512Bits(env.getProperty("vdf.seed.prng"));
//            this.statuscode = 5;
//        } else {
//            EntropyEntity byTimeStamp = entropyRepository.findByTimeStamp(timeStamp);
//            this.precommitment = byTimeStamp.getRawData();
//            this.statuscode = 0;
//        }



        CombinatioEnum combination = CombinatioEnum.valueOf(env.getProperty("pulse.vdf.combination").toUpperCase());
        int iterations = Integer.parseInt(env.getProperty("pulse.vdf.iterations"));

//        BigInteger x = null;

//        if (combination.equals(CombinatioEnum.XOR)){
//            x  = doXor(precommitment);
//        } else {
//            x = doConcat(precommitment);
//        }

        BigInteger y = doSloth(x, iterations);

        persist(y,x, iterations);
        seedList.clear();
        this.statusEnum = StatusEnum.STOPPED;

    }

    private BigInteger doConcat(String precommitment) {
        StringBuilder concat = new StringBuilder();
        for (VdfPulseDtoPost dto : seedList) {
            concat.append(dto.getSeed().toUpperCase().trim());
        }
        concat.append(precommitment.toUpperCase().trim());

        if (seedList.isEmpty()){
            return new BigInteger(precommitment, 16);
        }

        return new BigInteger(cipherSuite.getDigest(concat.toString()), 16);
    }

    // TODO Ta bungando com o XOR
    private BigInteger doXor(String precommitment){
        if (seedList.size()==0){
            return new BigInteger(precommitment, 16);
        }

        if (seedList.size()==1){
            byte[] xor = ByteUtils.xor(seedList.get(0).getSeed().getBytes(StandardCharsets.UTF_8), precommitment.getBytes(StandardCharsets.UTF_8));
            String s = ByteUtils.toHexString(xor);
            System.out.println(s);
            return new BigInteger(s, 16);
        }

//        for (VdfPulseDtoPost dto : seedList) {
//
//        }
        return null;
    }

    private BigInteger doSloth(BigInteger x, int iterations){
        BigInteger y = VdfSloth.mod_op(x, iterations);
        return y;
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
        vdfPulseEntity.setPeriod(Integer.parseInt(env.getProperty("pulse.vdf.period")));

        vdfPulseEntity.setX(x.toString());
        vdfPulseEntity.setY(y.toString());
        vdfPulseEntity.setIterations(iterations);

        // seed inmetro
        VdfPulseDtoPost vdfPulseDto1 = new VdfPulseDtoPost();
        vdfPulseDto1.setOriginEnum(OriginEnum.INMETRO);
        vdfPulseDto1.setSeed(this.precommitment);
        seedList.add(vdfPulseDto1);

        seedList.forEach(vdfPulseDto ->
                vdfPulseEntity.addSeed(new VdfSeedEntity(vdfPulseDto, vdfPulseEntity)));

        signPulse(VdfPulseSerialize.serializeVdfEntity(vdfPulseEntity), vdfPulseEntity);
        vdfPulsesRepository.saveAndFlush(vdfPulseEntity);
    }

    private void signPulse(byte[] bytes, VdfPulseEntity vdfPulseEntity) throws Exception {
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        vdfPulseEntity.setSignatureValue(cipherSuite.sign(privateKey, bytes));
    }

}

