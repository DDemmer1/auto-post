package de.demmer.dennis.autopost.services.scheduling;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private Map<Integer,ScheduledFuture<?>> tasks = new HashMap<>();

    public Post schedulePost(Post post) {

        int delay = getDelay(post);

        if(delay<0){
            post.setError(true);
            log.info("Delay " + delay + " post not scheduled" );
            return post;
        }

        TimerTask task = new PostTask(post.getUser(), post, facebookService,postRepository);
        ScheduledFuture<?> scheduledFuture = scheduler.schedule(task, delay, TimeUnit.SECONDS);
        log.info("Post " + post.getId() + " scheduled");
        if(post.isScheduled()){
            return post;
        } else if(post.isEnabled() && !post.isScheduled()){
            post.setScheduled(true);
            tasks.put(post.getId(),scheduledFuture);
        }

        return post;
    }


    public Post cancelScheduling(Post post){
        try{
            tasks.get(post.getId()).cancel(true);
            tasks.remove(post.getId());
            post.setScheduled(false);
            log.info("Scheduling of post " + post.getId() + " stopped");
        } catch (NullPointerException ne){
            log.info("Post " + post.getId() + " already canceled");
        }

        return post;
    }


    public int getDelay(Post post) {
        String dateNow = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        String timeNow = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());

        String datePost = post.getDate();
        String timePost = post.getTime() + ":00";


        LocalDateTime d1 = LocalDateTime.parse(datePost + " " + timePost, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime d2 = LocalDateTime.parse(dateNow + " " + timeNow, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Duration diff = Duration.between(d2, d1);
        log.info("Delay: "+ (int) diff.getSeconds()+ "s");
        return (int) diff.getSeconds();
    }

    public Map<Integer, ScheduledFuture<?>> getTasks() {
        return tasks;
    }
}
