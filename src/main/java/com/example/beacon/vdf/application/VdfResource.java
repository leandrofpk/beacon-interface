package com.example.beacon.vdf.application;

import com.example.beacon.vdf.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
public class VdfResource {

    private final VdfAppService vdfAppService;

    @Autowired
    public VdfResource(VdfAppService vdfAppService) {
        this.vdfAppService = vdfAppService;
    }

    @GetMapping("/current")
    public ResponseEntity submission(){
        try {
            VdfDto dto = converter(vdfAppService.getCurrentVdf());
            return new ResponseEntity(dto, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private VdfDto converter(Vdf vdf){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");

        VdfDto dto = new VdfDto();
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
