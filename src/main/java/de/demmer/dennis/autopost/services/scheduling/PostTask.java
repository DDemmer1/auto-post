package de.demmer.dennis.autopost.services.scheduling;


import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.TimerTask;


@Log4j2
@ToString
@Getter
@Setter
@NoArgsConstructor
public class PostTask extends TimerTask {

    @NotNull
    private User user;

    @NotNull
    private Post post;

    PostRepository postRepository;
    FacebookService facebookService;

    public PostTask(User user, Post post, FacebookService facebookService, PostRepository postRepository) {
        this.user = user;
        this.post = post;
        this.facebookService = facebookService;
        this.postRepository = postRepository;
    }

    @Override
    public void run() {

        if (post != null && user != null && !post.isPosted() && post.isScheduled() && post.isEnabled()) {
            facebookService.post(user, post);

            Post posted = postRepository.findByIdAndUserId(post.getId(), user.getId());
            posted.setPosted(true);
            posted.setEnabled(false);
            posted.setScheduled(false);
            postRepository.save(posted);

        } else {
            log.info("Not valid to post: " + post);
        }

    }
}
