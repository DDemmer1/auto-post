package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.User;
import de.demmer.dennis.autopost.properties.TestContext;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTests extends TestContext{


    @Autowired
    UserRepository userRepository;

    @Test
    public void findAllTest(){
        log.info(userRepository.findAll());
    }


}
