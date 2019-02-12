package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.PostGroup;
import de.demmer.dennis.autopost.entities.User;
import de.demmer.dennis.autopost.properties.TestProperties;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.ScheduleService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest

public class PostSchedulingTests {

    @Autowired
    TestProperties testProperties;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    FacebookService facebookService;



    @SneakyThrows
    @Test
    public void shouldPostIn10sec(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime plus5 = LocalDateTime.now().plusSeconds(10);
        LocalDateTime now = LocalDateTime.now();

        User testUser = new User(testProperties.getFbID(),testProperties.getAccessToken());
        PostGroup testGroup = new PostGroup("TestGroup","Group for JUnit tests", testUser);

        Post post = new Post("autoPost Unit Test vom " + dtf.format(now),DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(plus5),testGroup, testProperties.getPageID());
        testGroup.addPost(post);
        testGroup.setEnabled(true);

        scheduleService.scheduleGroup(testGroup);

        Thread.sleep(15000);

    }

}
