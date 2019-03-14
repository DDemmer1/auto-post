package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class ContactController {

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookService facebookService;


    @GetMapping(value = "/contact")
    public String contact(Model model){

        User activeUser = sessionService.getActiveUser();

        if (activeUser!=null) model.addAttribute("pageList", activeUser.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());


        return "contact";

    }
}
