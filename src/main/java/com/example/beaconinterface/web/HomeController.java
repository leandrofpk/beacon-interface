package com.example.beaconinterface.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ModelAndView root(){
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/home")
    public ModelAndView home(){
        return new ModelAndView("/home/index");
    }



}
