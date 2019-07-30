package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.repositories.FacebookuserRepository;
import de.demmer.dennis.autopost.services.facebook.FacebookSpringSocialService;
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
@Transactional
public class LoginController {

    @Autowired
    SessionService sessionService;

    @Autowired
    LoginService loginService;

    @Autowired
    FacebookSpringSocialService facebookService;

    @Autowired
    FacebookuserRepository userRepository;

    @Autowired
    FacebookpostRepository postRepository;

    @Autowired
    FacebookpageRepository pageRepository;


    @GetMapping(value = "/facebook")
    public String login(@RequestParam(value = "code") String code, Model model) {
        if (!code.isEmpty()) {
            try {
                loginService.login(code);
            } catch (Exception e) {
                model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
                e.printStackTrace();
                return "no-rights";
            }
        }
        return "redirect:/home";
    }


    @GetMapping(value = "/logout")
    public String logout(){
        sessionService.removeActiveUser();
        return "redirect:/home";
    }

    @GetMapping(value = "/deleteaccount")
    public String deleteAccount(){
        int id = sessionService.getActiveUser().getId();
        sessionService.removeActiveUser();
        userRepository.deleteById(id);
        return "redirect:/home";
    }



}
