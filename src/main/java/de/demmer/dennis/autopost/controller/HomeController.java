package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    FacebookService facebookService;

    @Autowired
    SessionService sessionService;


    @GetMapping(value = "/")
    public String home() {
        return "redirect:/home";
    }


    @GetMapping(value = "/home")
    public String home(Model model) {

        User activeUser = sessionService.getActiveUser();

        if (activeUser!=null) model.addAttribute("pageList", activeUser.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "home";
    }


}
