package de.demmer.dennis.autopost.properties;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.PostGroup;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@Component
@Transactional
public abstract class TestContext {

    @Autowired
    UserFactory userFactory;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestProperties testProperties;

    public User testUser;
    public PostGroup testGroup;
    public Post testPost;

    @PostConstruct
    public void init(){
        testUser = userFactory.getUser(testProperties.getAccessToken());
        testGroup = new PostGroup("TestGroup","Group for JUnit tests", testUser);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime plus5 = LocalDateTime.now().plusSeconds(10);
        testPost = new Post("autoPost Unit Test vom " + dtf.format(now), DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(plus5),testGroup, testProperties.getPageID());
        userRepository.save(testUser);
    }



}
