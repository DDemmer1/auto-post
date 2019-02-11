package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.User;
import de.demmer.dennis.autopost.properties.TestProperties;
import de.demmer.dennis.autopost.repositories.UserRepository;
import de.demmer.dennis.autopost.service.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class FacebookServiceTest {

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private TestProperties testProperties;


    private User testUser;


    @Before
    public void connectToFacebook_testAccessTokenGiven_requestedIdNotNull(){
        testUser = new User(testProperties.getFbID(),testProperties.getAccessToken(),"secret");
        Assert.assertTrue("No ID fetched. No connection to Facebook",!facebookService.getID(testUser).isEmpty());
        log.info("Facebook connection established");
    }


    @Test
    public void getID_testIDGiven_requestedIdShouldMatchGivenId(){
        String id = facebookService.getID(testUser);
        Assert.assertTrue("Facebook id should be: " + testProperties.getFbID()+" but is: " + id,id.equals(testProperties.getFbID()));
        log.info("Could get right Facebook ID. ID: " + id);
    }






}
