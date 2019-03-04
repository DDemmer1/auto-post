package de.demmer.dennis.autopost.service;

import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
public class LoginService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserFactory userFactory;

    @Autowired
    FacebookService facebookService;

    @Autowired
    SessionService sessionService;

    @Autowired
    PageRepository pageRepository;

    public void login(String code) {
        String accessToken = facebookService.createFacebookAccessToken(code);
        User user = userFactory.getUser(accessToken);
        sessionService.addActiveUser(user);
        updateUser(user);
    }


    private void updateUser(User user) {

        if (userRepository.findUserByFbId(user.getFbId()) == null) {
            //New User
            log.info("New user: " + user.getName());
            userRepository.save(user);
            user.getPageList().forEach(page -> pageRepository.save(page));

        } else {
            //Returning User
            log.info("Returning user: " + user.getName());
            User userInDB = userRepository.findUserByFbId(user.getFbId());
            int userId = userInDB.getId();
            BeanUtils.copyProperties(user, userInDB);
            userInDB.setId(userId);
            userRepository.save(userInDB);
        }
    }


}
