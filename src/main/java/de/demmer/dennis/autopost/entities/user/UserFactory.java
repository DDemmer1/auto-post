package de.demmer.dennis.autopost.entities.user;

import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.service.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
@Log4j2
@Component
public class UserFactory {

    @Autowired
    FacebookService facebookService;

    @Autowired
    PageRepository pageRepository;

    public User getUser(String oAuthToken){
        String id = facebookService.getID(oAuthToken);
        String name = facebookService.getName(oAuthToken);
        String email = facebookService.getEmail(oAuthToken);
        Map<String,String> pageIds = facebookService.getPageIds(oAuthToken);
        User user = new User(id,oAuthToken,name,email,pageIds);

        user.setPageList(facebookService.getPages(oAuthToken));
        user.getPageList().forEach(page -> page.setUser(user));

        return user;
    }
}
