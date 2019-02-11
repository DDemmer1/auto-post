package de.demmer.dennis.autopost.service;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.PostGroup;
import de.demmer.dennis.autopost.repositories.GroupRepository;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
@NoArgsConstructor
@Service
public class ScheduleService {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public void scheduleGroup(PostGroup group) {

        int userId = group.getUser().getId();

        for (Post post : group.getPosts()) {
            TimerTask task =  new PostTaskService(post.getUser(), post);
            scheduler.schedule(task, getDelay(post), TimeUnit.SECONDS);
        }
    }


    private int getDelay(Post post){
        LocalDateTime now = LocalDateTime.now();
        String nowString = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now);
        String timeToPost = post.getDate();

        LocalDateTime d1 = LocalDateTime.parse(timeToPost, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime d2 = LocalDateTime.parse(nowString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Duration diff = Duration.between(d2, d1);
        return (int)diff.getSeconds();
    }

}
