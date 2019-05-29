package de.demmer.dennis.autopost;


import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.properties.TestProperties;
import de.demmer.dennis.autopost.services.FacebookService;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFactoryTests {

    @Autowired
    UserFactory userFactory;

    @Autowired
    FacebookService facebookService;

    @Autowired
    private TestProperties testProperties;


    @Test
    public void getUserTest() throws UserException {
        Facebookuser user = userFactory.getUser(testProperties.getAccessToken());
        Assert.assertTrue("FBid of created User not valid",user.getFbId().equals(testProperties.getFbID()));
        log.info(user);
    }


}
