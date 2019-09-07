package com.example.beacon.vdf.application.old;

import com.example.beacon.interfac.infra.AppUri;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import com.example.beacon.vdf.repository.VdfPulsesRepository;
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
@RequestMapping("/vdf")
public class VdfController {

    private final AppUri appUri;

    private final VdfPulsesRepository vdfPulsesRepository;

    @Autowired
    public VdfController(AppUri appUri, VdfPulsesRepository vdfPulsesRepository) {
        this.appUri = appUri;
        this.vdfPulsesRepository = vdfPulsesRepository;
    }

    @GetMapping
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("vdf-pulse/index");

        mv.addObject("uri", appUri.getUri());

//        PulseDto pulseDto = querySinglePulsesService.findPrevious(ZonedDateTime.now().minus(5, ChronoUnit.MINUTES));
//        PulseEntity previous = vdfPulsesRepository.findPrevious(ZonedDateTime.now().minus(1, ChronoUnit.MINUTES));
        VdfPulseEntity previous = vdfPulsesRepository.findPrevious(ZonedDateTime.now().minus(1, ChronoUnit.MINUTES));

        if (previous!=null){
            mv.addObject("timestampPrevious", getTimeStampFormated(previous.getTimeStamp()));
            mv.addObject("timestampCurrent", getTimeStampFormated(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
            mv.addObject("pulseIndexPrevious", previous.getPulseIndex());
            mv.addObject("linkVerify", String.format("?y=%s&x=%s&iterations=%s",
                    previous.getY(), previous.getX(), previous.getIterations()));
        }

        return mv;
    }

    private String getTimeStampFormated(ZonedDateTime timestamp){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        String format = timestamp.withZoneSameInstant((ZoneOffset.UTC).normalized()).format(dateTimeFormatter);
        return format;
    }


}
