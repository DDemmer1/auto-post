package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {


    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldGiveID(){


    }


}
