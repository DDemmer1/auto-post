package de.demmer.dennis.autopost.controller;


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
public class LoginController{


    @Autowired
    HttpSession session;

    @GetMapping(value = "/facebook")
    public String login(Model model, @RequestParam(value = "code") String code){

        log.info(code);
        session.setAttribute("code",code);

        return "redirect:/home";
    }



}
