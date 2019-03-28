package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.properties.TestContext;
import de.demmer.dennis.autopost.properties.TestProperties;
import de.demmer.dennis.autopost.services.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class FacebookServiceTest extends TestContext {

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private TestProperties testProperties;


    @Before
    public void connectToFacebook_testAccessTokenGiven_requestedIdNotNull() {
        Assert.assertTrue("No ID fetched. No connection to Facebook", !facebookService.getID(testProperties.getAccessToken()).isEmpty());
        log.info("Facebook connection established");
    }


    @Test
    public void getID_testIDGiven_requestedIdShouldMatchGivenId() {
        String id = testUser.getFbId();
        Assert.assertTrue("Facebook id should be: " + testProperties.getFbID() + " but is: " + id, id.equals(testProperties.getFbID()));
        log.info("Valid Facebook ID. ID: " + id);
    }


    @Test
    public void getUserPictureTest() {
        facebookService.getProfilePicture(testProperties.getAccessToken());
    }

    @Test
    public void getPageProfilePicture() {
        String imageURL = facebookService.getPageProfilePicture(testProperties.getAccessToken(), testProperties.getPageID());
        log.info(imageURL);
    }


}
