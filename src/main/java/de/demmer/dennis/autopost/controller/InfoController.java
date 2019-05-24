package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InfoController {

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookService facebookService;

    @Autowired
    PostRepository postRepository;

    @GetMapping(value = "/about")
    public String getAbout(Model model) {

        User activeUser = sessionService.getActiveUser();

        if (activeUser != null) model.addAttribute("pageList", activeUser.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "about";
    }

    @GetMapping(value = "/services")
    public String getServices(Model model) {

        User activeUser = sessionService.getActiveUser();

        if (activeUser != null) model.addAttribute("pageList", activeUser.getPageList());

        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "services";
    }


    @GetMapping(value = "/status")
    public String getStatus(Model model) {

        User user = sessionService.getActiveUser();
        if (user == null) {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        } else if (user != null) model.addAttribute("pageList", user.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        model.addAttribute("user",user);
        model.addAttribute("scheduled",postRepository.findByScheduledAndUserId(true,user.getId()).size());
        model.addAttribute("enabled",postRepository.findByEnabledAndUserId(true,user.getId()).size());
        model.addAttribute("error",postRepository.findByErrorAndUserId(true,user.getId()).size());
        model.addAttribute("disabled",postRepository.findByEnabledAndPostedAndUserId(false,false,user.getId()).size());
        model.addAttribute("posted",postRepository.findByPostedAndUserId(true,user.getId()).size());

        return "status";
    }


    @GetMapping(value = "/settings")
    public String getSettings(Model model) {
        User user = sessionService.getActiveUser();
        if (user == null) {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        } else if (user != null) model.addAttribute("pageList", user.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "settings";
    }


    @GetMapping(value = "/tsvexample")
    public String getTsvExample(){
        return "tsvexample";
    }

}
