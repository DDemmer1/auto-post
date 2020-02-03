package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.services.BugReportService;
import de.demmer.dennis.autopost.services.facebook.FacebookService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Log4j2
@Controller
public class ErrorControllerImpl implements ErrorController {

    @Autowired
    BugReportService bugReportService;

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookService facebookService;


    /**
     * Is called when an unspecific error is thrown
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/error")
    public String error(Model model){
        Facebookuser activeUser = sessionService.getActiveUser();
        if (activeUser != null) {
            model.addAttribute("pageList", activeUser.getPageList());
            model.addAttribute("pageName", "Choose Facebook page");
        }
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
        return "error";
    }


    /**
     * Mapped by the "Send message" button on the bug report form
     *
     * @param firstName
     * @param lastName
     * @param replyAdress
     * @param bugType
     * @param message
     * @param model
     * @return
     */
    @PostMapping(value = "/bugreport")
    public String bugreport(@RequestParam(value = "firstName") String firstName,
                            @RequestParam(value = "lastName") String lastName,
                            @RequestParam(value = "replyAdress") String replyAdress,
                            @RequestParam(value = "bugType") String bugType,
                            @RequestParam(value = "message") String message, Model model){

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        builder.scheme("https");
        URI newUri = builder.build().toUri();

        bugReportService.sendMail(firstName,lastName,message,replyAdress,bugType,newUri.toString());

        Facebookuser activeUser = sessionService.getActiveUser();
        if (activeUser!=null) model.addAttribute("pageList", activeUser.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());



        return "mailsent";
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
