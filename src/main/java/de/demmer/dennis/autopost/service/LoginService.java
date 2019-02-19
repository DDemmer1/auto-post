package de.demmer.dennis.autopost.service;

import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
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


    public void login(){

    }

    private boolean userExists(){


        return true;
    }


}
