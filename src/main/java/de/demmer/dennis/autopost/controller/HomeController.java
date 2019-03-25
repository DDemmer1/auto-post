package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.LoginService;
import de.demmer.dennis.autopost.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    FacebookService facebookService;

    @Autowired
    SessionService sessionService;

    @Autowired
    UserFactory userFactory;


    @GetMapping(value = "/")
    public String home() {
        return "redirect:/home";
    }


    @GetMapping(value = "/home")
    public String home(Model model) {
        User activeUser = sessionService.getActiveUser();

        if (activeUser!=null) model.addAttribute("pageList", activeUser.getPageList());
        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());


        //dev
        if (activeUser!=null){

            for (Page page: activeUser.getPageList()) {
                List<Post> devPosts = new ArrayList<>();
                Post post = new Post("This is a test post for development","2018-12-11",page,"1");
                post.setImg("https://upload.wikimedia.org/wikipedia/commons/3/3d/FuBK-Testbild.png");
                post.setEnabled(true);
                post.setTime("11:10");


                Post post1 = new Post("This is a longer test post for development to test the scheduling table on autoPost","2018-12-11",page,"1");
                post1.setImg("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Cat03.jpg/1200px-Cat03.jpg");
                post1.setEnabled(false);
                post1.setPosted(false);
                post1.setLatitude(23);
                post1.setLongitude(60);
                post1.setTime("11:00");


                Post post2 = new Post("This is a test post for development with a dog picture","2017-12-11",page,"1");
                post2.setImg("https://i.kinja-img.com/gawker-media/image/upload/s--HqfzgkTd--/c_scale,f_auto,fl_progressive,q_80,w_800/wp2qinp6fu0d8guhex9v.jpg");
                post2.setEnabled(true);
                post2.setPosted(true);
                post2.setTime("11:00");


                devPosts.add(post);
                devPosts.add(post1);
                devPosts.add(post2);
                page.setPosts(devPosts);
            }
        }

        //dev

        return "home";
    }


}
