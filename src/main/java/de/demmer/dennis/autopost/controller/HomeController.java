package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    FacebookService facebookService;

    @Autowired
    UserFactory userFactory;


    @Autowired
    SessionService sessionService;


    @GetMapping(value="/")
    public String home(){
        sessionService.addActiveUser(userFactory.getUser("EAAeJZARgEZAQMBAAxA8vo9BAji74kRB6cUXxnpffFmpN6ZCKvuthon4zjZBiKVBhBIeQGpZB0L3mx9DT9hdxZAe9gph2XQXxSCQ2wZBAXZCPONTcyrEeGGAAAZCzQESvJNDuZCl7uF9kAy0YoVYAo7YbCHLZCLnPX8AD23QVKzZBjQg6DwZDZD"));


        return "redirect:/home";
    }

    @GetMapping(value="/home")
    public String home(Model model){


        model.addAttribute("loginlink",facebookService.createFacebookAuthorizationURL());


        return "home";
    }


    @GetMapping(value="/test")
    public String test(){




        return "index";
    }




}
