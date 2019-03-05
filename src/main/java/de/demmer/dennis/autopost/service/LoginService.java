package de.demmer.dennis.autopost.service;

import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        updateUser(user);
    }


    private void updateUser(User user) {

        if (userRepository.findUserByFbId(user.getFbId()) == null) {
            //New User
            sessionService.addActiveUser(newUserLogin(user));


        } else {
            //Returning User
            sessionService.addActiveUser(returningUserLogin(user));
        }
    }


    private User newUserLogin(User user){
        log.info("New user: " + user.getName());

        userRepository.save(user);
        user.getPageList().forEach(page -> pageRepository.save(page));
        return user;
    }


    private User returningUserLogin(User user){
        log.info("Returning user: " + user.getName());

        User userInDB = userRepository.findUserByFbId(user.getFbId());
        int tmpUserId = userInDB.getId();

        //check for new pages
        List<Page> newPages = getNewPages(user,userInDB);
        if(!newPages.isEmpty()){
            newPages.forEach(page -> pageRepository.save(page));
        }

        //check for pages to delete
        List<Page> pagesToDelete = getPagesToDelete(user,userInDB);
        if(!pagesToDelete.isEmpty()){
            pagesToDelete.forEach(page -> pageRepository.deleteByFbId(page.getFbId()));
        }

        //update user data
        BeanUtils.copyProperties(user, userInDB);
        userInDB.setId(tmpUserId);
        userRepository.save(userInDB);

        return userInDB;
    }

    private List<Page> getPagesToDelete(User user, User userInDB) {
        List<Page> pageListInDB = pageRepository.findByUserId(userInDB.getId());
        List<Page> pageListReturningUser  = user.getPageList();
        List<Page> pagesToDelete = new ArrayList<>();

        boolean pageToDelete = true;
        for (Page pageInDB : pageListInDB) {

            for (Page returningUserPage : pageListReturningUser) {
                if(pageInDB.equals(returningUserPage)) pageToDelete = false;
            }

            if(pageToDelete){
                pagesToDelete.add(pageInDB);
            }

            pageToDelete = true;
        }



        return pagesToDelete;
    }


    private List<Page> getNewPages(User user, User userInDB){

        List<Page> pageListInDB = pageRepository.findByUserId(userInDB.getId());
        List<Page> pageListReturningUser  = user.getPageList();
        List<Page> newPages = new ArrayList<>();

        boolean isNewPage = true;
        for (Page returningUserPage: pageListReturningUser) {

            for (Page dbPage : pageListInDB) {
                if (returningUserPage.equals(dbPage)) isNewPage = false;
            }

            if(isNewPage){
                returningUserPage.setUser(userInDB);
                newPages.add(returningUserPage);
            }
            isNewPage =true;
        }
        return newPages;
    }

}
