package de.demmer.dennis.autopost.services.userhandling;

import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.UserRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Transactional
@Log4j2
@Service
public class SessionService{

    @Autowired
    HttpSession session;

    @Autowired
    FacebookService facebookService;

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    PageRepository pageRepository;

    public void addActiveUser(User user){

        Map<String ,String> activeuser = new HashMap<>();
//        Map<String ,String> userPages = new HashMap<>();

        activeuser.put("id",user.getId()+"");
        activeuser.put("name",user.getName());
//        activeuser.put("email",user.getEmail());
//        activeuser.put("fbid",user.getFbId());
//        activeuser.put("oAuthToken",user.getOauthToken());
        activeuser.put("profilePic",facebookService.getProfilePicture(user.getOauthToken()));

        session.setAttribute("activeuser",activeuser);
//
//        pageRepository.findByUserId(user.getId()).forEach(page -> userPages.put(page.getFbId(),page.getName()));
//        session.setAttribute("userPages",userPages);

    }


    public void removeActiveUser(){
        session.removeAttribute("activeuser");
//        session.removeAttribute("userPages");
    }

    public User getActiveUser(){
        Map<String,String> userMap = (Map<String, String>) session.getAttribute("activeuser");
        if(userMap!=null){
            Integer userId = Integer.valueOf(userMap.get("id"));
            return userRepository.findUserById(userId);
        }  else return null;


    }


}
