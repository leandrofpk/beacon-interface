package com.example.beacon.interfac.web;

import com.example.beacon.interfac.api.dto.PulseDto;
import com.example.beacon.interfac.domain.service.QuerySinglePulsesService;
import com.example.beacon.interfac.infra.AppUri;
import com.example.beacon.vdf.infra.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZonedDateTime;
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
            mv.addObject("timestampPrevious", DateUtil.datetimeToMilli(pulseDto.getTimeStamp()));
            mv.addObject("timestampCurrent",  DateUtil.datetimeToMilli(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
            mv.addObject("pulseIndexPrevious", pulseDto.getPulseIndex());
        }

        return mv;
    }

}
