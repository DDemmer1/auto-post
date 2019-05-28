package de.demmer.dennis.autopost.properties;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.PageRepository;
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
    PageRepository pageRepository;

    @Autowired
    TestProperties testProperties;



    public User testUser;
    public Page testPage;
    public Post testPost;

    @PostConstruct
    public void init() throws UserException {
        testUser = userFactory.getUser(testProperties.getAccessToken());
        testPage = new Page();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtfContent = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime plus1 = LocalDateTime.now().plusMinutes(1);

        testPost = new Post();
        testPost.setContent("autoPost Unit Test vom " + dtfContent.format(now));
        testPost.setDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now));
        testPost.setTime(DateTimeFormatter.ofPattern("HH:mm").format(plus1));
        testPost.setUser(testUser);
        testPost.setEnabled(true);

        testPage.getFbposts().add(testPost);
        testPage.setUser(testUser);
        testPost.setPage(testPage);
        testPost.setPageID(testProperties.getPageID());

        userRepository.save(testUser);
    }



}
