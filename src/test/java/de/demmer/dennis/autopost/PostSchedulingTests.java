package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.properties.TestContext;
import de.demmer.dennis.autopost.properties.TestProperties;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void shouldPostIn1min(){
        scheduleService.schedulePost(testPost);
        Thread.sleep(60000);
    }


    @SneakyThrows
    @Test
    public void shouldNotPost(){
        scheduleService.schedulePost(testPost);
        scheduleService.cancelScheduling(testPost);
        Thread.sleep(70000);
    }


    @SneakyThrows
    @Test
    public void testDelay(){
        log.info("Difference is: " + scheduleService.getDelay(testPost));
    }

}
