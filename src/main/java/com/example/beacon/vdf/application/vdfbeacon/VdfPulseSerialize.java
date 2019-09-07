package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.vdf.application.vdfbeacon.dto.VdfPulseDtoPost;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import com.example.beacon.vdf.infra.entity.VdfSeedEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.example.beacon.shared.ByteSerializationFieldsUtil.*;
import static com.example.beacon.shared.ByteSerializationFieldsUtil.byteSerializeHash;

public class VdfPulseSerialize {
    public static byte[] serializeVdfDto(VdfPulseDtoPost dto) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048); // should be enough
        baos.write(byteSerializeHash(dto.getCertificateId()));
        baos.write(encode4(dto.getCipherSuite()));
        baos.write(byteSerializeString(dto.getOriginEnum().toString()));
        baos.write(byteSerializeHash(dto.getSeed()));
        return baos.toByteArray();
    }

    public static byte[] serializeVdfEntity(VdfPulseEntity entity) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048); // should be enough

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

        return baos.toByteArray();
    }

}
