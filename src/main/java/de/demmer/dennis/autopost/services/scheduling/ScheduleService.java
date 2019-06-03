package de.demmer.dennis.autopost.services.scheduling;

import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
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
//@NoArgsConstructor
@Service
public class ScheduleService {

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookpostRepository postRepository;

    @Autowired
    FacebookpostRepository facebookpostRepository;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    private Map<Integer, ScheduledFuture<?>> tasks = new HashMap<>();

    public Facebookpost schedulePost(Facebookpost post) {

        int delay = getDelay(post);

        if (delay < 0) {
            post.setError(true);
            log.info("Delay " + delay + " post not scheduled");
            return post;
        }

        TimerTask task = new PostTask(post.getFacebookuser(), post, facebookService, postRepository);
        ScheduledFuture<?> scheduledFuture = scheduler.schedule(task, delay, TimeUnit.SECONDS);
        log.info("Post " + post.getId() + " scheduled");
        if (post.isScheduled()) {
            cancelScheduling(post);
            post.setScheduled(true);
            tasks.put(post.getId(), scheduledFuture);
            return post;
        } else if (post.isEnabled() && !post.isScheduled()) {
            post.setScheduled(true);
            tasks.put(post.getId(), scheduledFuture);
        }

        return post;
    }


    public Facebookpost cancelScheduling(Facebookpost post) {
        try {
            tasks.get(post.getId()).cancel(true);
            tasks.remove(post.getId());
            post.setScheduled(false);
            log.info("Scheduling of post " + post.getId() + " stopped");
        } catch (NullPointerException ne) {
            log.info("Post " + post.getId() + " already canceled");
        }

        return post;
    }


    public int getDelay(Facebookpost post) {
        String dateNow = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        String timeNow = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());

        String datePost = post.getDate();
        String timePost = post.getTime() + ":00";


        LocalDateTime d1 = LocalDateTime.parse(datePost + " " + timePost, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime d2 = LocalDateTime.parse(dateNow + " " + timeNow, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Duration diff = Duration.between(d2, d1);
        log.info("Delay: " + (int) diff.getSeconds() + "s");
        return (int) diff.getSeconds();
    }

    public Map<Integer, ScheduledFuture<?>> getTasks() {
        return tasks;
    }


    public void scheduleAll() {
        List<Facebookpost> toSchedule = postRepository.findByEnabledAndPostedAndError(true, false, false);
        if (toSchedule != null) toSchedule.forEach(post -> schedulePost(post));
    }

    public void killAllTasks() {

        List<Integer> toRemove = new ArrayList<>();

        for (Map.Entry<Integer, ScheduledFuture<?>> entry : tasks.entrySet()) {
            Optional<Facebookpost> postOptional = facebookpostRepository.findById(entry.getKey());
            Facebookpost post = postOptional.get();
            tasks.get(post.getId()).cancel(true);
            toRemove.add(post.getId());
        }


        toRemove.forEach(id -> tasks.remove(id));
    }
}
