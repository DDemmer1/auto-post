package de.demmer.dennis.autopost.entities.user;

import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        List<Page> pageList = facebookService.getPages(oAuthToken);

        User user = new User(id,oAuthToken,name,email,pageList);
        user.getPageList().forEach(page -> page.setUser(user));

        return user;
    }
}
