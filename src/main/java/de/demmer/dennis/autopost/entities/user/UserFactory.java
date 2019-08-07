package de.demmer.dennis.autopost.entities.user;

import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.services.facebook.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = UserException.class)
@Log4j2
@Component
public class UserFactory {

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookpageRepository pageRepository;

    public Facebookuser getUser(String oAuthToken) throws UserException {

        String id = facebookService.getID(oAuthToken);
        String name = facebookService.getName(oAuthToken);
        String email = facebookService.getEmail(oAuthToken);
        List<Facebookpage> pageList = facebookService.getPages(oAuthToken);
        if (pageList == null) {
            throw new UserException("No facebookpage or insufficient rights to facebookpage.");
        }

        Facebookuser user = new Facebookuser(id, oAuthToken, name, email, pageList);
        user.getPageList().forEach(page -> page.setFacebookuser(user));
        return user;


    }
}
