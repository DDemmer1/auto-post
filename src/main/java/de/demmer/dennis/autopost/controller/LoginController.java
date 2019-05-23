package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.userhandling.LoginService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Log4j2
@Controller
@Transactional(rollbackFor = UserException.class)
public class LoginController {

    @Autowired
    SessionService sessionService;

    @Autowired
    LoginService loginService;

    @Autowired
    FacebookService facebookService;


    @GetMapping(value = "/facebook")
    public String login(@RequestParam(value = "code") String code, Model model) {
        if (!code.isEmpty()) {
            try {
                loginService.login(code);
            } catch (Exception e) {
                log.info("Catched");
//                model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
                return "redirect:/home";
            }
        }
        return "redirect:/home";
    }


    @GetMapping(value = "/logout")
    public String logout(){
        sessionService.removeActiveUser();
        return "redirect:/home";
    }


}
