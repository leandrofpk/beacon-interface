package com.example.beacon.vdf.application.vdfbeacon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/pulse/vdf")
public class VdfPulseController {

    @GetMapping
    public ModelAndView vdfPulse(){
        ModelAndView mv = new ModelAndView("vdf-pulse/index");
        return mv;
    }

}
