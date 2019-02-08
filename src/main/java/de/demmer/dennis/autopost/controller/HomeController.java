package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.service.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {


    @Autowired
    FacebookService facebookService;

    @GetMapping("/createFacebookAuthorization")
    public String createFacebookAuthorization(){
        return "<a href='" + facebookService.createFacebookAuthorizationURL() + "'> Login </a> ";
    }



    @GetMapping("/facebook")
    public String createFacebookAccessToken(@RequestParam("code") String code){

        System.out.println("Code lautet: " + code);
        facebookService.createFacebookAccessToken(code);


        return "<a href='/getName'>Show Name</a><br><a href='/getEmail'>Show Email</a>";
    }



    @GetMapping("/getName")
    public String getNameResponse(){
        return facebookService.getName();
    }


    @GetMapping("/getEmail")
    public String getEmailResponse(){
        return facebookService.getEmail();
    }



    @GetMapping("/post")
    public String setStatus(@RequestParam("post") String post){

        return "" + facebookService.postOnPage(post);

    }




}
