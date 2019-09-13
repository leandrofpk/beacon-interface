package com.example.beacon.vdf.application;

import com.example.beacon.interfac.infra.AppUri;
import com.example.beacon.vdf.infra.entity.CombinationEntity;
import com.example.beacon.vdf.infra.entity.VdfUnicornEntity;
import com.example.beacon.vdf.repository.CombinationRepository;
import com.example.beacon.vdf.repository.VdfUnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.beacon.vdf.infra.util.DateUtil.getTimeStampFormated;

@Controller
@RequestMapping
public class VdfController {

    private final AppUri appUri;

    private final CombinationRepository combinationRepository;

    private final VdfUnicornRepository vdfUnicornRepository;

    @Autowired
    public VdfController(AppUri appUri, CombinationRepository combinationRepository, VdfUnicornRepository vdfUnicornRepository) {
        this.appUri = appUri;
        this.combinationRepository = combinationRepository;
        this.vdfUnicornRepository = vdfUnicornRepository;
    }

    @GetMapping("/combination")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("vdf-combination/index");

        mv.addObject("uri", appUri.getUri());

        Long maxId = combinationRepository.findMaxId();
        CombinationEntity previous = combinationRepository.findByPulseIndex(--maxId);

        if (previous != null) {
            mv.addObject("timestampPrevious", getTimeStampFormated(previous.getTimeStamp()));
            mv.addObject("timestampCurrent", getTimeStampFormated(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
            mv.addObject("pulseIndexPrevious", previous.getPulseIndex());
            mv.addObject("linkVerify", String.format("?y=%s&x=%s&iterations=%s",
                    previous.getY(), previous.getX(), previous.getIterations()));
        }

        return mv;
    }

    @GetMapping("/unicorn")
    public ModelAndView homeClassic() {
        ModelAndView mv = new ModelAndView("vdf-unicorn/index");

        mv.addObject("uri", appUri.getUri());

        Long maxId = vdfUnicornRepository.findMaxId();
        VdfUnicornEntity previous = vdfUnicornRepository.findByPulseIndex(--maxId);


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
