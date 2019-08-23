package com.example.beaconinterface.api;

import com.example.beaconinterface.api.dto.PulseDto;
import com.example.beaconinterface.domain.service.QuerySinglePulsesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping(value = "/beacon/2.0/pulse", produces= MediaType.APPLICATION_JSON_VALUE)
public class SinglePulseResource {

    private final QuerySinglePulsesService singlePulsesService;

    @Autowired
    public SinglePulseResource(QuerySinglePulsesService singlePulsesService) {
        this.singlePulsesService = singlePulsesService;
    }

    @GetMapping("time/{timeStamp}")
    @ResponseBody
    public ResponseEntity specificTime(@PathVariable String timeStamp){
        try {
            ZonedDateTime parse = ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME);
            PulseDto byTimestamp = singlePulsesService.findSpecificTime(parse);

            if (byTimestamp==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(byTimestamp, HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("time/next/{timeStamp}")
    @ResponseBody
    public ResponseEntity next(@PathVariable String timeStamp){
        try {
            ZonedDateTime parse = ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME);
            PulseDto byTimestamp = singlePulsesService.findNext(parse);

            if (byTimestamp==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(byTimestamp, HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("time/previous/{timeStamp}")
    public ResponseEntity previous(@PathVariable String timeStamp){
        try {
            ZonedDateTime parse = ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME);
            PulseDto byTimestamp = singlePulsesService.findPrevious(parse);

            if (byTimestamp==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(byTimestamp, HttpStatus.OK);

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
            PulseDto byTimestamp = singlePulsesService.findLast();

            if (byTimestamp==null){
                return new ResponseEntity("Pulse Not Available.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(byTimestamp, HttpStatus.OK);

        } catch (DateTimeParseException e){
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
