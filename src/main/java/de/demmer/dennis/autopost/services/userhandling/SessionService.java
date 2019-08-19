package de.demmer.dennis.autopost.services.userhandling;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookuserRepository;
import de.demmer.dennis.autopost.services.facebook.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles a user session
 */

@Transactional
@Log4j2
@Service
public class SessionService{

    @Autowired
    HttpSession session;

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookuserRepository userRepository;


    /**
     * Adds the a some information about the user to the current Httpsession.
     * Is called at login.
     *
     * @param user
     */
    public void addActiveUser(Facebookuser user){

        Map<String ,String> activeuser = new HashMap<>();

        activeuser.put("id",user.getId()+"");
        activeuser.put("name",user.getName());
        activeuser.put("admin",user.getAdmin());

        activeuser.put("profilePic",facebookService.getProfilePicture(user.getOauthToken()));

        session.setAttribute("activeuser",activeuser);

    }


    /**
     * Removes the current active user from the Httpsession. Called at login or account deletion.
     */
    public void removeActiveUser(){
        session.removeAttribute("activeuser");
    }


    /**
     * Returns the current user in the Httpsession
     *
     * @return
     */
    public Facebookuser getActiveUser(){
        Map<String,String> userMap = (Map<String, String>) session.getAttribute("activeuser");
        if(userMap!=null){
            Integer userId = Integer.valueOf(userMap.get("id"));
            return userRepository.findFacebookuserById(userId);
        }  else return null;


    }


}
