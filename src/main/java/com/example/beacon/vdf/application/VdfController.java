package com.example.beacon.vdf.application;

import com.example.beacon.interfac.infra.AppUri;
import com.example.beacon.vdf.infra.entity.VdfPublicEntity;
import com.example.beacon.vdf.infra.entity.VdfPulseEntity;
import com.example.beacon.vdf.repository.VdfPulsesPublicRepository;
import com.example.beacon.vdf.repository.VdfPulsesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.beacon.vdf.infra.util.DateUtil.getTimeStampFormated;

@Controller
@RequestMapping("/vdf")
public class VdfController {

    private final AppUri appUri;

    private final VdfPulsesRepository vdfPulsesRepository;

    private final VdfPulsesPublicRepository vdfPulsesPublicRepository;

    @Autowired
    public VdfController(AppUri appUri, VdfPulsesRepository vdfPulsesRepository, VdfPulsesPublicRepository vdfPulsesPublicRepository) {
        this.appUri = appUri;
        this.vdfPulsesRepository = vdfPulsesRepository;
        this.vdfPulsesPublicRepository = vdfPulsesPublicRepository;
    }

    @GetMapping
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("vdf-pulse/index");

        mv.addObject("uri", appUri.getUri());

        VdfPulseEntity previous = vdfPulsesRepository.findPrevious(ZonedDateTime.now().minus(1, ChronoUnit.MINUTES));

        if (previous != null) {
            mv.addObject("timestampPrevious", getTimeStampFormated(previous.getTimeStamp()));
            mv.addObject("timestampCurrent", getTimeStampFormated(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
            mv.addObject("pulseIndexPrevious", previous.getPulseIndex());
            mv.addObject("linkVerify", String.format("?y=%s&x=%s&iterations=%s",
                    previous.getY(), previous.getX(), previous.getIterations()));
        }

        return mv;
    }

    @GetMapping("/public")
    public ModelAndView homeClassic() {
        ModelAndView mv = new ModelAndView("vdf-public/index");

        mv.addObject("uri", appUri.getUri());

        VdfPublicEntity previous = vdfPulsesPublicRepository.findPrevious(ZonedDateTime.now().minus(1, ChronoUnit.MINUTES));

        if (previous != null) {
            mv.addObject("timestampPrevious", getTimeStampFormated(previous.getTimeStamp()));
            mv.addObject("timestampCurrent", getTimeStampFormated(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
            mv.addObject("pulseIndexPrevious", previous.getPulseIndex());
            mv.addObject("linkVerify", String.format("?y=%s&x=%s&iterations=%s",
                    previous.getY(), previous.getX(), previous.getIterations()));
        }

        return mv;
    }
}
