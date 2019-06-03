package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Handels the pages with generall informations like "about", "services" or the "Status" in the user menu
 */

//TODO refactor 'settings' to 'account'


@Controller
public class InfoController {

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookpostRepository postRepository;

    @GetMapping(value = "/about")
    public String getAbout(Model model) {

        Facebookuser activeUser = sessionService.getActiveUser();

        if (activeUser != null) model.addAttribute("pageList", activeUser.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "about";
    }

    @GetMapping(value = "/services")
    public String getServices(Model model) {

        Facebookuser activeUser = sessionService.getActiveUser();

        if (activeUser != null) model.addAttribute("pageList", activeUser.getPageList());

        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "services";
    }


    /**
     * Mapped by the "Status" button in the user drop down menu
     * @param model
     * @return
     */
    @GetMapping(value = "/status")
    public String getStatus(Model model) {

        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        } else if (user != null) model.addAttribute("pageList", user.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        model.addAttribute("user",user);
        model.addAttribute("scheduled",postRepository.findByScheduledAndFacebookuserId(true,user.getId()).size());
        model.addAttribute("enabled",postRepository.findByEnabledAndFacebookuserId(true,user.getId()).size());
        model.addAttribute("error",postRepository.findByErrorAndFacebookuserId(true,user.getId()).size());
        model.addAttribute("disabled",postRepository.findByEnabledAndPostedAndFacebookuserId(false,false,user.getId()).size());
        model.addAttribute("posted",postRepository.findByPostedAndFacebookuserId(true,user.getId()).size());

        return "status";
    }


    /**
     * Leads to the option to delete a user account
     * @param model
     * @return
     */
    @GetMapping(value = "/settings")
    public String getSettings(Model model) {
        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        } else if (user != null) model.addAttribute("pageList", user.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "settings";
    }


    /**
     * Mapped by the "Example tsv" button in the tsvform template
     * Returns a example tsv file.
     * @return
     */
    @GetMapping(value = "/tsvexample")
    public String getTsvExample(){
        return "tsvexample";
    }

}
