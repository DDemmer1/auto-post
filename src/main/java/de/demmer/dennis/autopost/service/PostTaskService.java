package de.demmer.dennis.autopost.service;


import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.User;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.TimerTask;


@Log4j2
@ToString
@Getter
@Setter
@Service
@NoArgsConstructor
public class PostTaskService extends TimerTask{

    @Autowired
    private FacebookService facebookService;

    @NotNull
    private User user;

    @NotNull
    private Post post;

    public PostTaskService(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Override
    public void run() {
        log.info("Post mit Inhalt: " + post.getContent() + " , wurde von User: " + user.getFbId() + " gepostet");
        new FacebookService().post(user,post);
    }
}
