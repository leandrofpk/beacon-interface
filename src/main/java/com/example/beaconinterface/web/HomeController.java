package com.example.beaconinterface.web;

import com.example.beaconinterface.api.dto.PulseDto;
import com.example.beaconinterface.domain.service.QuerySinglePulsesService;
import com.example.beaconinterface.infra.AppUri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/")
public class HomeController {

    private final AppUri appUri;

    private final QuerySinglePulsesService querySinglePulsesService;

    @Autowired
    public HomeController(AppUri appUri, QuerySinglePulsesService querySinglePulsesService) {
        this.appUri = appUri;
        this.querySinglePulsesService = querySinglePulsesService;
    }

    @GetMapping
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("home/index");
        mv.addObject("uri", appUri.getUri());

        PulseDto pulseDto = querySinglePulsesService.findPrevious(ZonedDateTime.now().minus(1, ChronoUnit.MINUTES));

        if (pulseDto!=null){
            mv.addObject("timestampPrevious", getTimeStampFormated(pulseDto.getTimeStamp()));
            mv.addObject("timestampCurrent", getTimeStampFormated(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
            mv.addObject("pulseIndexPrevious", pulseDto.getPulseIndex());
        }

        return mv;
    }

    private String getTimeStampFormated(ZonedDateTime timestamp){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        String format = timestamp.withZoneSameInstant((ZoneOffset.UTC).normalized()).format(dateTimeFormatter);
        return format;
    }


}
