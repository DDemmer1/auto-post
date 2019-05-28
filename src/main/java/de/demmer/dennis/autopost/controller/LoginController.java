package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.repositories.UserRepository;
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
//@Transactional(rollbackFor = UserException.class)
@Transactional
public class LoginController {

    @Autowired
    SessionService sessionService;

    @Autowired
    LoginService loginService;

    @Autowired
    FacebookService facebookService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PageRepository pageRepository;


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
