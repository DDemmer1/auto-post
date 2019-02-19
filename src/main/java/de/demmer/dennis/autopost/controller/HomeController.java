package de.demmer.dennis.autopost.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

@Autowired
    HttpSession session;



    @GetMapping(value="/home")
    public String home(Model model){

        String code = session.getAttribute("code").toString();
        model.addAttribute("code",code);

        return "home";
    }

}
