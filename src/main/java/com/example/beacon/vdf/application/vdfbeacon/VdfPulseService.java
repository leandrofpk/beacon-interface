package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.CriptoUtilService;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfTest3Wladmir2;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import com.example.beacon.vdf.infra.entity.VdfSeedEntity;
import com.example.beacon.vdf.infra.util.HashUtil;
import com.example.beacon.vdf.repository.VdfPulsesRepository;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.example.beacon.shared.ByteSerializationFieldsUtil.*;

@Service
public class VdfPulseService {

    private final Environment env;

    private StatusEnum statusEnum;

    private List<VdfPulseDto> seedList;

    private final VdfPulsesRepository vdfPulsesRepository;

    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";

    private ZonedDateTime timestamp;

    private String precommitment = "";

    @Autowired
    public VdfPulseService(Environment env, VdfPulsesRepository vdfPulsesRepository) {
        this.env = env;
        this.vdfPulsesRepository = vdfPulsesRepository;
        this.statusEnum = StatusEnum.STOPPED;
        this.seedList = new ArrayList<>();
    }

    public void startTimeSlot(){
        this.statusEnum.equals(StatusEnum.OPEN);
        this.timestamp = getDateTime();
    }

    public void endTimeSlot() throws Exception {
        this.statusEnum = StatusEnum.RUNNING;
        run();
    }

    public boolean verify(BigInteger x, BigInteger y, int iterations){

        return true;
    }

    public boolean isOpen(){
        return this.statusEnum.equals(StatusEnum.OPEN);
    }

    public void addSeed(VdfPulseDto dto){
        this.seedList.add(dto);
    }

    private void run() throws Exception {
        this.precommitment = "0EE710ADDAFA8774268E736A92B65C82901087BD926886147179BAD110ADCA5EFDE28099C94DEC2EE2A328369A72737C564C6C3DA08CE7057DC9B7B1D02BFBB2";

        CombinatioEnum combination = CombinatioEnum.valueOf(env.getProperty("pulse.vdf.combination").toUpperCase());
        int iterations = Integer.parseInt(env.getProperty("pulse.vdf.iterations"));

        BigInteger x = null;

        if (combination.equals(CombinatioEnum.XOR)){
            x  = doXor(precommitment);
        } else {
            x = doConcat(precommitment);
        }

        BigInteger y = doSloth(x, iterations);

        persist(y,x, iterations);
    }

    private BigInteger doConcat(String precommitment) {
        StringBuilder concat = new StringBuilder();
        for (VdfPulseDto dto : seedList) {
            concat.append(dto.getSeed().toUpperCase().trim());
        }
        concat.append(precommitment.toUpperCase().trim());

        if (seedList.isEmpty()){
            return new BigInteger(precommitment, 16);
        }

        return new BigInteger(HashUtil.getDigest(concat.toString()), 16);
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

//        for (VdfPulseDto dto : seedList) {
//
//        }
        return null;
    }

    private BigInteger doSloth(BigInteger x, int iterations){
        System.out.println(x);

        VdfTest3Wladmir2 vdfSloth = new VdfTest3Wladmir2();
        BigInteger y = vdfSloth.mod_op(x, iterations);

        boolean b = vdfSloth.mod_verif(y, x, iterations);
        System.out.println("Verified? " + b);

        System.out.println("x:" + x);
        System.out.println("y:" + y);
        System.out.println("iterations:" + iterations);

        System.out.println("Persist");
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

        vdfPulseEntity.setStatusCode(0);

        vdfPulseEntity.setX(x.toString());
        vdfPulseEntity.setY(y.toString());
        vdfPulseEntity.setIterations(iterations);

        // seed inmetro
        VdfPulseDto vdfPulseDto1 = new VdfPulseDto();
        vdfPulseDto1.setOriginEnum(OriginEnum.INMETRO);
        vdfPulseDto1.setSeed(this.precommitment);
        seedList.add(vdfPulseDto1);

        seedList.forEach(vdfPulseDto ->
                vdfPulseEntity.addSeed(new VdfSeedEntity(vdfPulseDto, vdfPulseEntity)));

        ByteArrayOutputStream baos = serializeFields(vdfPulseEntity);

        signPulse(baos, vdfPulseEntity);


        vdfPulsesRepository.saveAndFlush(vdfPulseEntity);
    }

    private void signPulse(ByteArrayOutputStream baos, VdfPulseEntity vdfPulseEntity) throws Exception {
        final ICipherSuite sha512Util = CipherSuiteBuilder.build(vdfPulseEntity.getCipherSuite());
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));
        String digest = sha512Util.getDigest(baos.toByteArray());
        vdfPulseEntity.setSignatureValue(sha512Util.signBytes15(digest, privateKey));

    }

    private ZonedDateTime getDateTime(){
        ZonedDateTime now = ZonedDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plus(1, ChronoUnit.MINUTES)
                .withZoneSameInstant((ZoneOffset.UTC).normalized());

        return now;
    }

    private ByteArrayOutputStream serializeFields(VdfPulseEntity entity) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(8192); // should be enough

        baos.write(byteSerializeHash(entity.getCertificateId()));
        baos.write(encode4(entity.getCipherSuite()));
        baos.write(encode4(entity.getPeriod()));
        baos.write(encode8(entity.getPulseIndex()));
        baos.write(byteSerializeString(getTimeStampFormated(entity.getTimeStamp())));
        baos.write(encode8(entity.getStatusCode()));

        for (VdfSeedEntity e : entity.getSeedList()) {
            baos.write(byteSerializeHash(e.getSeed()));
            baos.write(byteSerializeString(e.getOrigin().toString()));
        }

        baos.write(byteSerializeString(entity.getX()));
        baos.write(byteSerializeString(entity.getY()));
        baos.write(encode4(entity.getIterations()));

        return baos;
    }

}

