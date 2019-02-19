package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.UserRepository;
import de.demmer.dennis.autopost.service.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Transactional
@Controller
public class DevController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FacebookService facebookService;


    @GetMapping(value="/")
    public String devLogin(Model model){


        model.addAttribute("loginlink",facebookService.createFacebookAuthorizationURL());


        return "login";
    }
}
