package com.example.beaconinterface.web;

import com.example.beaconinterface.AppUri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {

    private final AppUri appUri;

    @Autowired
    public HomeController(AppUri appUri) {
        this.appUri = appUri;
    }

    @GetMapping
    public ModelAndView root(){
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/home")
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("/home/index");
        mv.addObject("uri", appUri.getUri());
        return mv;
    }



}
