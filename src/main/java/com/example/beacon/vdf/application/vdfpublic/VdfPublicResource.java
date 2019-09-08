package com.example.beacon.vdf.application.vdfpublic;

import com.example.beacon.vdf.infra.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/vdf/", produces= MediaType.APPLICATION_JSON_VALUE)
public class VdfPublicResource {

    private final VdfPublicService vdfPublicService;

    @Autowired
    public VdfPublicResource(VdfPublicService vdfPublicService) {
        this.vdfPublicService = vdfPublicService;
    }

    @GetMapping("/current")
    public ResponseEntity submission(){
        try {
            VdfDtoOld dto = converter(vdfPublicService.getCurrentVdf());
            return new ResponseEntity(dto, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private VdfDtoOld converter(Vdf vdf){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");

        VdfDtoOld dto = new VdfDtoOld();
        dto.setStart(vdf.getSubmissionTime().getStart().withZoneSameInstant((ZoneOffset.UTC).normalized()).format(dateTimeFormatter));
        dto.setEnd(vdf.getSubmissionTime().getEnd().withZoneSameInstant((ZoneOffset.UTC).normalized()).format(dateTimeFormatter));
        dto.setStatus(vdf.getStatusEnum().getDescription());
        dto.setCurrentHash(vdf.getCurrentHash());
        dto.setOutput(vdf.getOutput());
        dto.setNextRunInMinutes(DateUtil.getMinutesForNextRun(ZonedDateTime.now(), vdf.getSubmissionTime().getStart()));

        dto.setSeedList(vdf.getSeedList());

        return dto;
    }

}
