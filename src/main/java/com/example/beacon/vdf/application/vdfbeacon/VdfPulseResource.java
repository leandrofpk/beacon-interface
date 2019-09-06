package com.example.beacon.vdf.application.vdfbeacon;

import com.example.beacon.interfac.api.dto.PulseDto;
import com.example.beacon.interfac.infra.PulseEntity;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import com.example.beacon.vdf.repository.VdfPulsesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping(value = "/beacon/2.0/pulse/vdf/", produces= MediaType.APPLICATION_JSON_VALUE)
public class VdfPulseResource {

    private final VdfPulsesRepository vdfPulsesRepository;

    public VdfPulseResource(VdfPulsesRepository vdfPulsesRepository) {
        this.vdfPulsesRepository = vdfPulsesRepository;
    }

    @GetMapping("time/{timeStamp}")
    @ResponseBody
    public ResponseEntity specificTime(@PathVariable String timeStamp){
        try {
            ZonedDateTime parse = ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME);

            VdfPulseEntity byTimeStamp = vdfPulsesRepository.findByTimeStamp(parse);

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

        Long first = vdfPulsesRepository.findFirst();
        VdfPulseEntity byPulseIndex = vdfPulsesRepository.findByPulseIndex(first);

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

            VdfPulseEntity next = vdfPulsesRepository.findNext(parse);

            if (next==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }

            VdfPulseEntity byTimeStamp = vdfPulsesRepository.findByTimeStamp(next.getTimeStamp());
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

            VdfPulseEntity previous = vdfPulsesRepository.findPrevious(parse);

            if (previous==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }

            VdfPulseEntity byTimeStamp = vdfPulsesRepository.findByTimeStamp(previous.getTimeStamp());
            return new ResponseEntity(convertToDto(byTimeStamp), HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("last")
    @ResponseBody
    public ResponseEntity last(){
        try {

            Long maxId = vdfPulsesRepository.findMaxId();

            VdfPulseEntity byPulseIndex = vdfPulsesRepository.findByPulseIndex(maxId);

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

    private VdfPulseDto convertToDto(VdfPulseEntity entity){
        VdfPulseDto dto = new VdfPulseDto();

        dto.setCertificateId(entity.getCertificateId());
        dto.setCipherSuite(entity.getCipherSuite());
        dto.setPulseIndex(entity.getPulseIndex());
        dto.setTimeStamp(getTimeStampFormated(entity.getTimeStamp()));
        dto.setSignatureValue(entity.getSignatureValue());
        dto.setPeriod(entity.getPeriod());

        entity.getSeedList().forEach(s ->
                 dto.addSeed(new VdfSeedDto(s.getSeed(), s.getOrigin().toString())));

        dto.setSlothDto(new VdfSlothDto(entity.getX(), entity.getY(), entity.getIterations()));
        dto.setStatusCode(entity.getStatusCode());

        return dto;
    }

    private String getTimeStampFormated(ZonedDateTime timestamp){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        String format = timestamp.withZoneSameInstant((ZoneOffset.UTC).normalized()).format(dateTimeFormatter);
        return format;
    }

}
