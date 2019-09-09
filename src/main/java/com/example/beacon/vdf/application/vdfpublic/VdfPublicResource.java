package com.example.beacon.vdf.application.vdfpublic;

import com.example.beacon.vdf.application.vdfbeacon.dto.VdfPulseDto;
import com.example.beacon.vdf.application.vdfbeacon.dto.VdfSeedDto;
import com.example.beacon.vdf.application.vdfbeacon.dto.VdfSlothDto;
import com.example.beacon.vdf.infra.entity.VdfPublicEntity;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import com.example.beacon.vdf.infra.util.DateUtil;
import com.example.beacon.vdf.repository.VdfPulsesPublicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.example.beacon.vdf.infra.util.DateUtil.getTimeStampFormated;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/beacon/2.0/pulse/vdf/public", produces= MediaType.APPLICATION_JSON_VALUE)
public class VdfPublicResource {

    private final VdfPublicService vdfPublicService;

    private final VdfPulsesPublicRepository vdfPulsesPublicRepository;

    @Autowired
    public VdfPublicResource(VdfPublicService vdfPublicService, VdfPulsesPublicRepository vdfPulsesPublicRepository) {
        this.vdfPublicService = vdfPublicService;
        this.vdfPulsesPublicRepository = vdfPulsesPublicRepository;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/current")
    public ResponseEntity submission(){
        try {
            VdfDtoOld dto = converterCurrent(vdfPublicService.getCurrentVdf());
            return new ResponseEntity(dto, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("time/{timeStamp}")
    @ResponseBody
    public ResponseEntity specificTime(@PathVariable String timeStamp){
        try {
            ZonedDateTime parse = ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME);

            VdfPublicEntity byTimeStamp = vdfPulsesPublicRepository.findByTimeStamp(parse);

            if (byTimeStamp==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(convertToDto(byTimeStamp), HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/first")
    @ResponseBody
    public ResponseEntity first(){

        Long first = vdfPulsesPublicRepository.findFirst();
        VdfPublicEntity byPulseIndex = vdfPulsesPublicRepository.findByPulseIndex(first);

        if (byPulseIndex==null){
            return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(convertToDto(byPulseIndex), HttpStatus.OK);
    }

    @GetMapping("/next/{timeStamp}")
    @ResponseBody
    public ResponseEntity next(@PathVariable String timeStamp){
        try {
            ZonedDateTime parse = ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME);

            VdfPublicEntity next = vdfPulsesPublicRepository.findNext(parse);

            if (next==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }

            VdfPublicEntity byTimeStamp = vdfPulsesPublicRepository.findByTimeStamp(next.getTimeStamp());
            return new ResponseEntity(convertToDto(byTimeStamp), HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/previous/{timeStamp}")
    public ResponseEntity previous(@PathVariable String timeStamp){
        try {
            ZonedDateTime parse = ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME);

            VdfPublicEntity previous = vdfPulsesPublicRepository.findPrevious(parse);

            if (previous==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }

            VdfPublicEntity byTimeStamp = vdfPulsesPublicRepository.findByTimeStamp(previous.getTimeStamp());
            return new ResponseEntity(convertToDto(byTimeStamp), HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/last")
    @ResponseBody
    public ResponseEntity last(){
        try {

            Long maxId = vdfPulsesPublicRepository.findMaxId();

            VdfPublicEntity byPulseIndex = vdfPulsesPublicRepository.findByPulseIndex(maxId);

            if (byPulseIndex==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(convertToDto(byPulseIndex), HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    ResponseEntity postPublicSeed(@Valid @RequestBody SeedPostDto seedPostDto) {
        try {

            if (!vdfPublicService.isOpen()){
                return new ResponseEntity("Not open", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ResponseEntity("Internal server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        vdfPublicService.addSeed(seedPostDto);
        return new ResponseEntity("Created", HttpStatus.CREATED);
    }

    private VdfDtoOld converterCurrent(Vdf vdf){
        VdfDtoOld dto = new VdfDtoOld();
        dto.setStart(getTimeStampFormated(vdf.getSubmissionTime().getStart()));
        dto.setEnd(getTimeStampFormated(vdf.getSubmissionTime().getEnd()));
        dto.setStatus(vdf.getStatusEnum());
        dto.setCurrentHash(vdf.getCurrentHash());
//        dto.setOutput(vdf.getOutput());
        dto.setNextRunInMinutes(DateUtil.getMinutesForNextRun(ZonedDateTime.now(), vdf.getSubmissionTime().getStart()));
        dto.setSeedList(vdf.getSeedList());
        return dto;
    }

    private VdfPulseDto convertToDto(VdfPublicEntity entity){
        VdfPulseDto dto = new VdfPulseDto();

        dto.setCertificateId(entity.getCertificateId());
        dto.setCipherSuite(entity.getCipherSuite());
        dto.setPulseIndex(entity.getPulseIndex());
        dto.setTimeStamp(getTimeStampFormated(entity.getTimeStamp()));
        dto.setSignatureValue(entity.getSignatureValue());
        dto.setPeriod(entity.getPeriod());

        entity.getSeedList().forEach(s ->
                dto.addSeed(new VdfSeedDto(s.getSeed(), s.getDescription())));

        dto.setSlothDto(new VdfSlothDto(entity.getX(), entity.getY(), entity.getIterations()));

        return dto;
    }

}
