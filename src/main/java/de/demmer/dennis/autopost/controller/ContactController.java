package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Simple Controller which loads the contact page.
 */

//TODO Extend with a contact formular

@Log4j2
@Controller
public class ContactController {

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookService facebookService;


    @GetMapping(value = "/contact")
    public String contact(Model model){

        Facebookuser activeUser = sessionService.getActiveUser();

        if (activeUser!=null) model.addAttribute("pageList", activeUser.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());


        return "contact";

    }
}
