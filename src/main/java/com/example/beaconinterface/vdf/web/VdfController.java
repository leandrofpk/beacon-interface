package com.example.beaconinterface.vdf.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/vdf")
public class VdfController {

    @GetMapping
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("vdf/index-vdf");
        return mv;
    }

}
