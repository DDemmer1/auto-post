package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.PostGroup;
import de.demmer.dennis.autopost.properties.TestContext;
import de.demmer.dennis.autopost.properties.TestProperties;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.ScheduleService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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
public class PostSchedulingTests extends TestContext{

    @Autowired
    TestProperties testProperties;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    FacebookService facebookService;



    @SneakyThrows
    @Test
    public void shouldPostIn10sec(){

        testGroup.addPost(testPost);
        testGroup.setEnabled(true);
        scheduleService.scheduleGroup(testGroup);

        Thread.sleep(15000);

    }

}
