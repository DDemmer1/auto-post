package de.demmer.dennis.autopost.services.scheduling;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Log4j2
@NoArgsConstructor
@Service
public class ScheduleService {

    @Autowired
    FacebookService facebookService;

    @Autowired
    PostRepository postRepository;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    Map<Integer,ScheduledFuture<?>> tasks = new HashMap<>();

    @Transactional
    public Post schedulePost(Post post) {
        TimerTask task = new PostTask(post.getUser(), post, facebookService,postRepository);
        ScheduledFuture<?> scheduledFuture = scheduler.schedule(task, getDelay(post), TimeUnit.SECONDS);

        if(post.isScheduled() && post.isEnabled()){
            cancelScheduling(post);
            post.setScheduled(false);
        } else if(post.isEnabled()){
            post.setScheduled(true);
            tasks.put(post.getId(),scheduledFuture);
        }

        return post;
    }


    public Post cancelScheduling(Post post){
        tasks.get(post.getId()).cancel(true);
        post.setScheduled(false);
        tasks.remove(post);
        return post;
    }


    public int getDelay(Post post) {


        String dateNow = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        String timeNow = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());

        log.info("DateNow: "+ dateNow);
        log.info("TimeNow: "+ timeNow);

        String datePost = post.getDate();
        String timePost = post.getTime() + ":00";
        log.info("****************");

        log.info("DatePost: "+ datePost);
        log.info("TimePost: "+ timePost);


        LocalDateTime d1 = LocalDateTime.parse(datePost + " " + timePost, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime d2 = LocalDateTime.parse(dateNow + " " + timeNow, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Duration diff = Duration.between(d2, d1);
        log.info("Delay: "+ (int) diff.getSeconds());
        return (int) diff.getSeconds();
    }

}
