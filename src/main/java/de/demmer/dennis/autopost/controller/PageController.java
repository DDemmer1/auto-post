package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.tsvimport.TsvService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Transactional
@Log4j2
@Controller
public class PageController {


    @Autowired
    SessionService sessionService;

    @Autowired
    PageRepository pageRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FacebookService facebookService;

    @Autowired
    TsvService tsvService;

    @Autowired
    ScheduleService scheduleService;

    @GetMapping(value = "/schedule/{id}")
    public String postList(@PathVariable(value = "id") String id, Model model) {

        Facebookuser user = sessionService.getActiveUser();

        model.addAttribute("page", pageRepository.findByFbId(id));

        if (user != null) {
            List<Facebookpost> posts = pageRepository.findByFbId(id).getFacebookposts();
            Collections.sort(posts);
            model.addAttribute("pageList", user.getPageList());
            model.addAttribute("postList", posts);
        } else {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        }

        return "page";
    }


    @GetMapping(value = "/schedule/{id}/selected")
    public ModelAndView changeSelected(@PathVariable(value = "id") String id, ModelMap modelMap, @RequestParam Map<String, String> params) {

        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            modelMap.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return new ModelAndView("no-login", modelMap);
        }

        String action = params.get("action");
        List<Integer> postIds = new ArrayList<>();

        params.entrySet().forEach((entry) -> {
            if (entry.getValue().equals("on")) {
                postIds.add(Integer.parseInt(entry.getKey()));
            }
        });

        if (action.equals("delete"))
            postIds.forEach((postId) ->{
                Facebookpost post = postRepository.findByIdAndPageFbId(postId, id);
                scheduleService.cancelScheduling(post);

                postRepository.deleteByIdAndPageFbId(postId, id);
            });

        if (action.equals("disable"))
            postIds.forEach((postId) -> {
                Facebookpost post = postRepository.findByIdAndPageFbId(postId, id);
                scheduleService.cancelScheduling(post);

                post.setEnabled(false);
                postRepository.save(post);
            });


        if (action.equals("enable"))
            postIds.forEach((postId) -> {
                Facebookpost post = postRepository.findByIdAndPageFbId(postId, id);
                post.setEnabled(true);
                scheduleService.schedulePost(post);
                postRepository.save(post);
            });

        return new ModelAndView("redirect:/schedule/" + id, modelMap);
    }


}
