package de.demmer.dennis.autopost.entities.user;


import de.demmer.dennis.autopost.service.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class UserFactory {


    @Autowired
    FacebookService facebookService;



    public User getUser(String oAuthToken){
        String id = facebookService.getID(oAuthToken);
        String name = facebookService.getName(oAuthToken);
        String email = facebookService.getEmail(oAuthToken);
        Map<String,String> pageIds = facebookService.getPageIds(oAuthToken);


        return new User(id,oAuthToken,name,email,pageIds);
//        return new User(id,oAuthToken,name,email);
    }

}
