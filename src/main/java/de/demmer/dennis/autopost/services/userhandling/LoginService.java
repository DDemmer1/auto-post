package de.demmer.dennis.autopost.services.userhandling;

import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookuserRepository;
import de.demmer.dennis.autopost.services.facebook.FacebookSpringSocialService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles everything user related at login.
 * Checks for changed user informations if a user returns and creates new user if it is the first login.
 */
@Log4j2
@Service
@Transactional
public class LoginService {

    @Autowired
    FacebookuserRepository userRepository;

    @Autowired
    UserFactory userFactory;

    @Autowired
    FacebookSpringSocialService facebookService;

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookpageRepository pageRepository;


    /**
     * Logs a user into the web app
     *
     * @param code Needs the return code from the facebook login link
     * @throws UserException
     */
    public void login(String code) throws UserException {
        String accessToken = facebookService.createFacebookAccessToken(code);
        Facebookuser user = userFactory.getUser(accessToken);
        updateUser(user);
    }


    /*
     * Adds a new user to the database and current session, or updates the informations of a returning user and adds him to the session
     */
    private void updateUser(Facebookuser user) {

        if (userRepository.findFacebookuserByFbId(user.getFbId()) == null) {
            //New User
            sessionService.addActiveUser(newUserLogin(user));


        } else {
            //Returning User
            sessionService.addActiveUser(returningUserLogin(user));
        }
    }

    //adds a new user
    private Facebookuser newUserLogin(Facebookuser user) {
        log.info("New fbuser: " + user.getName());

        userRepository.save(user);
        user.getPageList().forEach((page) -> {
            pageRepository.save(page);
        });
        return user;
    }


    //checks if returning user needs update. If so saved it into database
    private Facebookuser returningUserLogin(Facebookuser user) {
        log.info("Returning fbuser: " + user.getName());

        Facebookuser userInDB = userRepository.findFacebookuserByFbId(user.getFbId());
        int tmpUserId = userInDB.getId();

        //check for new pages
        List<Facebookpage> newPages = getNewPages(user, userInDB);
        if (!newPages.isEmpty()) {
            newPages.forEach((page) -> {
                pageRepository.save(page);
            });
        }

        //check for pages to delete or remove admin
        List<Facebookpage> pagesToDelete = getPagesToDelete(user, userInDB);
        if (!pagesToDelete.isEmpty()) {
            pagesToDelete.forEach((page) -> {
                pageRepository.deleteByFbId(page.getFbId());
            });
        }

        //update fbuser data
        BeanUtils.copyProperties(user, userInDB);
        userInDB.setId(tmpUserId);
        userRepository.save(userInDB);

        return userInDB;
    }


    //checks if facebook pages where deleted
    private List<Facebookpage> getPagesToDelete(Facebookuser user, Facebookuser userInDB) {
        List<Facebookpage> pageListInDB = pageRepository.findByFacebookuserId(userInDB.getId());
        List<Facebookpage> pageListReturningUser = user.getPageList();
        List<Facebookpage> pagesToDelete = new ArrayList<>();

        boolean pageToDelete = true;
        for (Facebookpage pageInDB : pageListInDB) {

            for (Facebookpage returningUserPage : pageListReturningUser) {
                if (pageInDB.equals(returningUserPage)) pageToDelete = false;
            }

            if (pageToDelete) {
                pagesToDelete.add(pageInDB);
            }

            pageToDelete = true;
        }


        return pagesToDelete;
    }


    //checks if new Facebook pages where created
    private List<Facebookpage> getNewPages(Facebookuser user, Facebookuser userInDB) {

        List<Facebookpage> pageListInDB = pageRepository.findByFacebookuserId(userInDB.getId());
        List<Facebookpage> pageListReturningUser = user.getPageList();
        List<Facebookpage> newPages = new ArrayList<>();

        boolean isNewPage = true;
        for (Facebookpage returningUserPage : pageListReturningUser) {

            for (Facebookpage dbPage : pageListInDB) {
                if (returningUserPage.equals(dbPage)) isNewPage = false;
            }

            if (isNewPage) {
                returningUserPage.setFacebookuser(userInDB);
                newPages.add(returningUserPage);
            }
            isNewPage = true;
        }
        return newPages;
    }

}
