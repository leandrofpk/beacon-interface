package com.example.beacon.vdf.application.old;

import com.example.beacon.vdf.StatusEnum;
import com.example.beacon.vdf.SubmissionTime;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.infra.util.DateUtil;
import com.example.beacon.vdf.infra.util.HashUtil;
import lombok.Getter;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Vdf {

    private StatusEnum statusEnum;

    private SubmissionTime submissionTime;

    private List<VdfSeed> seedList = new ArrayList<>();

    private String currentHash;

    private byte[] currentXorValue = new byte[4096];

    private String output;

    public Vdf(){
        this.statusEnum = StatusEnum.CLOSED;
        this.submissionTime = new SubmissionTime(DateUtil.getTimestampOfNextRun(ZonedDateTime.now()), 15);
        this.currentHash = "";
    }

    public void startSubmissions(int submissionDuration){
        this.statusEnum = StatusEnum.OPEN;
        this.currentHash = "";
        this.output = "";
    }

    public void addSeed(VdfSeed seed){

        if (!this.statusEnum.equals(StatusEnum.OPEN)){
            throw new VdfException("Status <> OPEN");
        }

        this.seedList.add(seed);

        if (seedList.size()== 1){
            this.currentXorValue = seed.getSeed().getBytes(StandardCharsets.UTF_8);
        } else {
            this.currentXorValue = ByteUtils.xor(currentXorValue, seed.getSeed().getBytes(StandardCharsets.UTF_8));
        }
        this.currentHash = HashUtil.getDigest(currentXorValue);
    }

    public void startProcessing(){
        this.statusEnum = StatusEnum.RUNNING;

//        BigInteger x = new BigInteger("80");

        BigInteger x = new BigInteger(ByteUtils.toHexString(this.currentXorValue), 16);

        VdfSloth vdfSloth = new VdfSloth();

//        BigInteger bigInteger = vdfSloth.mod_op(x, 999999);
        BigInteger bigInteger = vdfSloth.mod_op(x, 9);
        this.output = HashUtil.getDigest(bigInteger.toByteArray());
        System.out.println("output:" + this.output);

        endProcess();
        this.statusEnum = StatusEnum.CLOSED;
    }

    private void endProcess() {
        // persistir resultados
        System.out.println("persistir resultados");
    }

    private void run(){

    }



}
