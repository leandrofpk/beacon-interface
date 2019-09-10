package com.example.beacon.vdf.application.combination;

import com.example.beacon.vdf.application.combination.dto.VdfPulseDtoPost;
import com.example.beacon.vdf.infra.entity.CombinationEntity;
import com.example.beacon.vdf.infra.entity.CombinationSeedEntity;
import com.example.beacon.vdf.infra.entity.VdfUnicornEntity;
import com.example.beacon.vdf.infra.entity.VdfUnicornSeedEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.example.beacon.shared.ByteSerializationFieldsUtil.*;
import static com.example.beacon.vdf.infra.util.DateUtil.getTimeStampFormated;

public class VdfSerialize {
    public static byte[] serializeVdfDto(VdfPulseDtoPost dto) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048); // should be enough
        baos.write(byteSerializeHash(dto.getCertificateId()));
        baos.write(encode4(dto.getCipherSuite()));
        baos.write(byteSerializeString(dto.getOriginEnum().toString()));
        baos.write(byteSerializeHash(dto.getSeed()));
        return baos.toByteArray();
    }

    public static byte[] serializeCombinationEntity(CombinationEntity entity) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048); // should be enough

        baos.write(byteSerializeHash(entity.getCertificateId()));
        baos.write(encode4(entity.getCipherSuite()));
        baos.write(encode4(entity.getPeriod()));
        baos.write(encode8(entity.getPulseIndex()));
        baos.write(byteSerializeString(getTimeStampFormated(entity.getTimeStamp())));
        baos.write(encode8(entity.getStatusCode()));

        for (CombinationSeedEntity e : entity.getSeedList()) {
            baos.write(byteSerializeHash(e.getSeed()));
            baos.write(byteSerializeString(e.getDescription()));
        }

        baos.write(byteSerializeString(entity.getX()));
        baos.write(byteSerializeString(entity.getY()));
        baos.write(encode4(entity.getIterations()));

        return baos.toByteArray();
    }

    public static byte[] serializeVdfPublic(VdfUnicornEntity entity) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048); // should be enough

        baos.write(byteSerializeHash(entity.getCertificateId()));
        baos.write(encode4(entity.getCipherSuite()));
        baos.write(encode4(entity.getPeriod()));
        baos.write(encode8(entity.getPulseIndex()));
        baos.write(byteSerializeString(getTimeStampFormated(entity.getTimeStamp())));

        for (VdfUnicornSeedEntity e : entity.getSeedList()) {
            baos.write(byteSerializeHash(e.getSeed()));
            baos.write(byteSerializeString(e.getDescription()));
        }

        baos.write(byteSerializeString(entity.getX()));
        baos.write(byteSerializeString(entity.getY()));
        baos.write(encode4(entity.getIterations()));

        return baos.toByteArray();
    }

}
