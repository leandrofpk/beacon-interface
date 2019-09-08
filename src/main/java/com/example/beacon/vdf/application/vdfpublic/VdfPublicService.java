package com.example.beacon.vdf.application.vdfpublic;

import com.example.beacon.shared.CipherSuiteBuilder;
import com.example.beacon.shared.EntropyRepository;
import com.example.beacon.shared.ICipherSuite;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.vdfbeacon.CombinatioEnum;
import com.example.beacon.vdf.application.vdfbeacon.StatusEnum;
import com.example.beacon.vdf.sources.SeedBuilder;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.beacon.vdf.infra.util.DateUtil.getCurrentTrucatedZonedDateTime;

@Service
public class VdfPublicService {

    private final Environment env;

    private Vdf vdf;

    private StatusEnum statusEnum;

    private ZonedDateTime timestamp;

    private List<SeedPostDto> seedList;

    private final ICipherSuite cipherSuite;

    private String currentHash;

    private byte[] currentXorValue = new byte[128];

    private String currentConcatValue;

    private final EntropyRepository entropyRepository;

    private final SeedBuilder seedBuilder;

    @Autowired
    public VdfPublicService(Environment environment, EntropyRepository entropyRepository, SeedBuilder seedBuilder) {
        this.env = environment;
        this.entropyRepository = entropyRepository;
        this.seedBuilder = seedBuilder;
        this.statusEnum = StatusEnum.STOPPED;
        this.vdf = new Vdf();
        this.seedList = new ArrayList<>();
        this.cipherSuite = CipherSuiteBuilder.build(0);
    }

    public void startTimeSlot() throws Exception {
        this.statusEnum = StatusEnum.OPEN;
        this.timestamp = getCurrentTrucatedZonedDateTime();

        List<SeedPostDto> preDefinedSeeds = seedBuilder.getPreDefSeed();
        preDefinedSeeds.forEach(dto -> calcSeed(dto));
    }

    public void addSeed(SeedPostDto dto){
        this.seedList.add(dto);
        calcSeed(dto);
    }

    public void endTimeSlot() throws Exception {
        this.statusEnum = StatusEnum.RUNNING;

        List<SeedPostDto> preDefinedSeeds = seedBuilder.getHonestParty();
        preDefinedSeeds.forEach(dto -> calcSeed(dto));

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

    }


    public Vdf getCurrentVdf(){
        return this.vdf;
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
    protected void persist(BigInteger y, BigInteger x, int iterations) {
        System.out.println("persistir");
    }

}
