package com.example.beacon.interfac.api;

import com.example.beacon.interfac.api.dto.PulseDto;
import com.example.beacon.interfac.api.dto.SkiplistDto;
import com.example.beacon.interfac.domain.service.BadRequestException;
import com.example.beacon.interfac.domain.service.QuerySequencePulsesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping(value = "/beacon/2.0/skiplist", produces= MediaType.APPLICATION_JSON_VALUE)
public class SequenceOfPulsesResource {

    private final QuerySequencePulsesService querySequencePulsesService;

    @Autowired
    public SequenceOfPulsesResource(QuerySequencePulsesService querySequencePulsesService) {
        this.querySequencePulsesService = querySequencePulsesService;
    }

    @GetMapping("time/{startTimestamp}/{endTimestamp}")
    @ResponseBody
    public ResponseEntity skypList(@PathVariable String startTimestamp, @PathVariable String endTimestamp){
        try {
            ZonedDateTime parse1 = ZonedDateTime.parse(startTimestamp, DateTimeFormatter.ISO_DATE_TIME);
            ZonedDateTime parse2 = ZonedDateTime.parse(endTimestamp, DateTimeFormatter.ISO_DATE_TIME);

            List<PulseDto> sequence = querySequencePulsesService.sequence(parse1, parse2);

            SkiplistDto skiplist = new SkiplistDto(sequence);

            if (sequence==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(skiplist, HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);}
        catch (BadRequestException e){
            return new ResponseEntity("Bad Request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
