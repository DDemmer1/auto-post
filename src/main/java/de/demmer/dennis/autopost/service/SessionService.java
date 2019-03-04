package de.demmer.dennis.autopost.service;

import de.demmer.dennis.autopost.entities.user.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Log4j2
@Service
public class SessionService{

    @Autowired
    HttpSession session;

    @Autowired
    FacebookService facebookService;

    public void addActiveUser(User user){

        Map<String ,String> activeuser = new HashMap<>();

        activeuser.put("name",user.getName());
        activeuser.put("email",user.getEmail());
        activeuser.put("fbid",user.getFbId());
        activeuser.put("oAuthToken",user.getOauthToken());
        activeuser.put("profilePic",facebookService.getProfilePicture(user.getOauthToken()));

        session.setAttribute("activeuser",activeuser);
        session.setAttribute("userPages",user.getPages());

    }


    public void removeActiveUser(){
        session.removeAttribute("activeuser");
        session.removeAttribute("userPages");
    }


}
