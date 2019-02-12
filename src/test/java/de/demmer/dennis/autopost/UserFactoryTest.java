package de.demmer.dennis.autopost;


import de.demmer.dennis.autopost.entities.User;
import de.demmer.dennis.autopost.entities.UserFactory;
import de.demmer.dennis.autopost.properties.TestProperties;
import de.demmer.dennis.autopost.service.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFactoryTest {

    @Autowired
    UserFactory userFactory;

    @Autowired
    FacebookService facebookService;

    @Autowired
    private TestProperties testProperties;




    @Test
    public void getUserTest(){

        User user = userFactory.getUser(testProperties.getAccessToken());
        log.info(user);

    }


}
