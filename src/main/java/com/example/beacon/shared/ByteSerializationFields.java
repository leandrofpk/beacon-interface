package com.example.beacon.shared;

import com.example.beacon.interfac.api.dto.PulseDto;
import com.example.beacon.vdf.infra.entity.CombinationEntity;
import com.example.beacon.vdf.infra.entity.CombinationSeedEntity;
import com.example.beacon.vdf.infra.entity.VdfUnicornEntity;
import com.example.beacon.vdf.infra.entity.VdfUnicornSeedEntity;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.example.beacon.vdf.infra.util.DateUtil.getTimeStampFormated;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ByteSerializationFields {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public ByteSerializationFields(PulseDto pulse) throws IOException {
        byteSerializeString(pulse.getUri());
        byteSerializeString(pulse.getVersion());
        encode4(pulse.getCipherSuite());
        encode4(pulse.getPeriod());
        byteSerializeHash(pulse.getCertificateId());
        encode8(pulse.getChainIndex());
        encode8(pulse.getPulseIndex());
        byteSerializeString(getTimeStampFormated(pulse.getTimeStamp()));
        byteSerializeHash(pulse.getLocalRandomValue());
        byteSerializeHash(pulse.getExternal().getSourceId());
        encode4(pulse.getExternal().getStatusCode());
        byteSerializeHash(pulse.getExternal().getValue());
        byteSerializeHash(pulse.getListValues().get(0).getValue());
        byteSerializeHash(pulse.getListValues().get(1).getValue());
        byteSerializeHash(pulse.getListValues().get(2).getValue());
        byteSerializeHash(pulse.getListValues().get(3).getValue());
        byteSerializeHash(pulse.getListValues().get(4).getValue());
        byteSerializeHash(pulse.getPrecommitmentValue());
        encode4(pulse.getStatusCode());
    }

    public ByteSerializationFields(CombinationEntity entity) throws IOException {
        byteSerializeHash(entity.getCertificateId());
        encode4(entity.getCipherSuite());
        byteSerializeString(entity.getVersion());
        encode4(entity.getPeriod());
        byteSerializeHash(entity.getCertificateId());
        encode8(entity.getPulseIndex());
        byteSerializeString(getTimeStampFormated(entity.getTimeStamp()));
        byteSerializeString(entity.getCombination());
        for (CombinationSeedEntity e : entity.getSeedList()) {
            byteSerializeString(getTimeStampFormated(e.getTimeStamp()));
            byteSerializeHash(e.getSeed());
            byteSerializeHash(e.getCumulativeHash());
        }

        byteSerializeString(entity.getP());
        byteSerializeString(entity.getX());
        byteSerializeString(entity.getY());
        encode4(entity.getIterations());
    }

    public ByteSerializationFields(VdfUnicornEntity entity) throws IOException {
        byteSerializeHash(entity.getCertificateId());
        encode4(entity.getCipherSuite());
        byteSerializeString(entity.getVersion());
        encode4(entity.getPeriod());
        byteSerializeHash(entity.getCertificateId());
        encode8(entity.getPulseIndex());
        byteSerializeString(getTimeStampFormated(entity.getTimeStamp()));
        byteSerializeString(entity.getCombination());
        for (VdfUnicornSeedEntity e : entity.getSeedList()) {
            byteSerializeString(getTimeStampFormated(e.getTimeStamp()));
            byteSerializeHash(e.getSeed());
            byteSerializeHash(e.getCumulativeHash());
        }

        byteSerializeString(entity.getP());
        byteSerializeString(entity.getX());
        byteSerializeString(entity.getY());
        encode4(entity.getIterations());
    }

    public ByteArrayOutputStream getBaos(){
        return baos;
    }


    private void encode4(int value) throws IOException {
        baos.write(ByteBuffer.allocate(4).putInt(value).array());
    }

    private void encode8(long value) throws IOException {
        baos.write(ByteBuffer.allocate(8).putLong(value).array());
    }

    private void byteSerializeHash(String hash) throws IOException {
        int bLenHash =  ByteUtils.fromHexString(hash).length;
        baos.write(ByteBuffer.allocate(4).putInt(bLenHash).array());
        baos.write(ByteUtils.fromHexString(hash));
    }

    public ByteArrayOutputStream byteSerializeSig(String hexString) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bLenHash = ByteUtils.fromHexString(hexString).length;
        baos.write(ByteBuffer.allocate(4).putInt(bLenHash).array());
        baos.write(ByteUtils.fromHexString(hexString));
        return baos;
    }

    private void byteSerializeString(String value) throws IOException {
        int bytLen = value.getBytes(UTF_8).length;
        baos.write(ByteBuffer.allocate(4).putInt(bytLen).array());
        baos.write(value.getBytes(UTF_8));
    }

}
