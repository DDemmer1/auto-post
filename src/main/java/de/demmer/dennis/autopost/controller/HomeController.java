package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.facebook.FacebookService;
import de.demmer.dennis.autopost.services.userhandling.LoginService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controls the base page of the web app.
 * Paragraphs marked with 'DEV' can be uncommented to activate instant login of the admin for testing purposes
 */
@Controller
public class HomeController {

    @Autowired
    FacebookService facebookService;

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookpostRepository postRepository;

    //----------DEV----------//
//    @Autowired
//    UserFactory userFactory;
//
//    @Autowired
//    LoginService loginService;
//
//    @Value("${test.accessToken}")
//    String devAccessToken;
    //----------DEV----------//

    @GetMapping(value = "/")
    public String home() {
        return "redirect:/home";
    }


    @GetMapping(value = "/home")
    public String home(Model model) {

        Facebookuser user = sessionService.getActiveUser();

        if (user != null) {
            model.addAttribute("pageList", user.getPageList());
            model.addAttribute("pageName", "Choose Facebook page");
            model.addAttribute("pageList", user.getPageList());
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            model.addAttribute("user",user);
            model.addAttribute("scheduled",postRepository.findByScheduledAndFacebookuserId(true,user.getId()).size());
            model.addAttribute("enabled",postRepository.findByEnabledAndFacebookuserId(true,user.getId()).size());
            model.addAttribute("error",postRepository.findByErrorAndFacebookuserId(true,user.getId()).size());
            model.addAttribute("disabled",postRepository.findByEnabledAndPostedAndFacebookuserId(false,false,user.getId()).size());
            model.addAttribute("posted",postRepository.findByPostedAndFacebookuserId(true,user.getId()).size());
        }

        //----------DEV----------//
//        else {
//            try {
//                user = userFactory.getUser(devAccessToken);
//            } catch (UserException e) {
//                e.printStackTrace();
//            }
//            user.setAdmin(true);
//            sessionService.addActiveUser(user);
//            loginService.updateUser(user);
//        }
        //----------DEV----------//

        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "home";
    }


}
