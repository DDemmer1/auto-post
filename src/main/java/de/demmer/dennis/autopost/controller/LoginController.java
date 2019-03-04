package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.LoginService;
import de.demmer.dennis.autopost.service.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;


@Log4j2
@Controller
public class LoginController {

    @Autowired
    SessionService sessionService;

    @Autowired
    LoginService loginService;


    @GetMapping(value = "/facebook")
    public String login(@RequestParam(value = "code") String code) {
        if (!code.isEmpty()) {
            loginService.login(code);
        }
        return "redirect:/home";
    }


    @GetMapping(value = "/logout")
    public String logout(){
        sessionService.removeActiveUser();
        return "redirect:/home";
    }


}
