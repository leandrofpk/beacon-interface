package com.example.beacon.vdf.application.vdfunicorn;

import com.example.beacon.vdf.application.VdfPulseDto;
import com.example.beacon.vdf.application.VdfSeedDto;
import com.example.beacon.vdf.application.combination.dto.VdfSlothDto;
import com.example.beacon.vdf.infra.entity.VdfUnicornEntity;
import com.example.beacon.vdf.infra.util.DateUtil;
import com.example.beacon.vdf.repository.VdfUnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.example.beacon.vdf.infra.util.DateUtil.getTimeStampFormated;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/beacon/2.0/vdf/unicorn", produces= MediaType.APPLICATION_JSON_VALUE)
public class VdfUnicornResource {

    private final VdfUnicornService vdfUnicornService;

    private final VdfUnicornRepository vdfUnicornRepository;

    @Autowired
    public VdfUnicornResource(VdfUnicornService vdfUnicornService, VdfUnicornRepository vdfUnicornRepository) {
        this.vdfUnicornService = vdfUnicornService;
        this.vdfUnicornRepository = vdfUnicornRepository;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/current")
    public ResponseEntity submission(){
        try {
            UnicornCurrentDto dto = vdfUnicornService.getUnicornState();

            return new ResponseEntity(dto, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("pulse/{pulseIndex}")
    public ResponseEntity byPulseIndex(@PathVariable String pulseIndex) {
        try {
            VdfUnicornEntity byPulseIndex = vdfUnicornRepository.findByPulseIndex(Long.parseLong(pulseIndex));
            if (byPulseIndex == null) {
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(convertToDto(byPulseIndex), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("time/{timeStamp}")
    @ResponseBody
    public ResponseEntity specificTime(@PathVariable String timeStamp){
        try {
            ZonedDateTime parse = ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME);

            VdfUnicornEntity byTimeStamp = vdfUnicornRepository.findByTimeStamp(parse);

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

        Long first = vdfUnicornRepository.findFirst();
        VdfUnicornEntity byPulseIndex = vdfUnicornRepository.findByPulseIndex(first);

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

            VdfUnicornEntity next = vdfUnicornRepository.findNext(parse);

            if (next==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }

            VdfUnicornEntity byTimeStamp = vdfUnicornRepository.findByTimeStamp(next.getTimeStamp());
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

            VdfUnicornEntity previous = vdfUnicornRepository.findPrevious(parse);

            if (previous==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }

            VdfUnicornEntity byTimeStamp = vdfUnicornRepository.findByTimeStamp(previous.getTimeStamp());
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

            Long maxId = vdfUnicornRepository.findMaxId();

            VdfUnicornEntity byPulseIndex = vdfUnicornRepository.findByPulseIndex(maxId);

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

            if (!vdfUnicornService.isOpen()){
                return new ResponseEntity("Not open", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ResponseEntity("Internal server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        vdfUnicornService.addSeed(seedPostDto);
        return new ResponseEntity("Created", HttpStatus.CREATED);
    }

    private VdfPulseDto convertToDto(VdfUnicornEntity entity){
        VdfPulseDto dto = new VdfPulseDto();

        dto.setUri(entity.getUri());
        dto.setVersion(entity.getVersion());
        dto.setCertificateId(entity.getCertificateId());
        dto.setCipherSuite(entity.getCipherSuite());
        dto.setPulseIndex(entity.getPulseIndex());
        dto.setTimeStamp(getTimeStampFormated(entity.getTimeStamp()));
        dto.setSignatureValue(entity.getSignatureValue());
        dto.setPeriod(entity.getPeriod());
        dto.setCombination(entity.getCombination());

        entity.getSeedList().forEach(s ->
                dto.addSeed(new VdfSeedDto(s.getSeed(),
                        DateUtil.getTimeStampFormated(s.getTimeStamp()),
                        s.getDescription(), s.getUri(),
                        s.getCumulativeHash())));

        dto.setSlothDto(new VdfSlothDto(entity.getP(), entity.getX(), entity.getY(), entity.getIterations()));

        return dto;
    }

}
