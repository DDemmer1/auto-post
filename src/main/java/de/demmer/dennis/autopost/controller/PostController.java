package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Log4j2
public class PostController {

    @Autowired
    PageRepository pageRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookService facebookService;


    @GetMapping(value = "/schedule/{pageFbId}/{postId}")
    public String addPost(@PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") String postId, Model model){

        User user = sessionService.getActiveUser();

        if(user != null){
            Page page = pageRepository.findByFbId(pageFbId);
            Post post = postRepository.findByIdAndUserId(Integer.valueOf(postId),user.getId());
            model.addAttribute("pageList", user.getPageList());
            model.addAttribute("post",post);
            model.addAttribute("page",page);
        } else{
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        }


        return "post";
    }

}
