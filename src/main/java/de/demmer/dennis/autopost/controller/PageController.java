package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.facebook.FacebookService;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.tsvimport.TsvService;
import de.demmer.dennis.autopost.services.userhandling.FacebookuserService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


/**
 * Handels the user interaction with the 'page' template
 */
@Transactional
@Log4j2
@Controller
public class PageController {


    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookpageRepository pageRepository;

    @Autowired
    FacebookpostRepository postRepository;

    @Autowired
    FacebookService facebookService;

    @Autowired
    TsvService tsvService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    FacebookuserService facebookuserService;

    /**
     * Shows the posts of a page in the 'page' template
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "/schedule/{id}")
    public String postList(@PathVariable(value = "id") String id, Model model, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "end", required = false) Integer end, @RequestParam(value = "active", defaultValue = "1") Integer active) {

        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            return "no-login";
        }
        //Check if user is page Admin
        if (!facebookuserService.isAdminOfPage(id, user)) {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            model.addAttribute("pageList", user.getPageList());
            return "no-rights";
        }

        model.addAttribute("page", pageRepository.findByFbIdAndFacebookuser_Id(id, user.getId()));


        //start and end of sublist of posts
        start = (start == null) ? 0 : start;
        end = (end == null) ? 20 : end;

        if (user != null) {
//            List<Facebookpost> posts = pageRepository.findByFbIdAndFacebookuser_Id(id,user.getId()).getFacebookposts();
            List<Facebookpost> posts = new ArrayList<>();
            for (Facebookpage page : pageRepository.findAllByFbId(id)) {
                posts.addAll(page.getFacebookposts());
            }


            Collections.sort(posts);
            model.addAttribute("pageList", user.getPageList());

            //check if start/ end is in range of list size
            start = (start > posts.size()) ? posts.size() - 20 : start;
            end = (end > posts.size()) ? posts.size() : end;

            model.addAttribute("postList", posts.subList(start, end));
            model.addAttribute("numPosts", posts.size());
            model.addAttribute("active", active);
        } else {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        }


        return "page";
    }


    /**
     * Mapped by the 'selected' dropdown button in the 'page' template
     *
     * @param id
     * @param modelMap
     * @param params
     * @return
     */
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
            postIds.forEach((postId) -> {
                Facebookpost post = postRepository.findByIdAndFacebookpageFbId(postId, id);
                scheduleService.cancelScheduling(post);
                postRepository.deleteByIdAndFacebookpageFbId(postId, id);
            });

        if (action.equals("disable"))
            postIds.forEach((postId) -> {
                Facebookpost post = postRepository.findByIdAndFacebookpageFbId(postId, id);
                scheduleService.cancelScheduling(post);
                post.setEnabled(false);
                postRepository.save(post);
            });


        if (action.equals("enable"))
            postIds.forEach((postId) -> {
                Facebookpost post = postRepository.findByIdAndFacebookpageFbId(postId, id);
                post.setEnabled(true);
                post.setPosted(false);
                scheduleService.schedulePost(post);
                postRepository.save(post);
            });

        return new ModelAndView("redirect:/schedule/" + id, modelMap);
    }



    @PostMapping(value = "/schedule/{id}/deleteunposted")
    public String deleteUnposted(@PathVariable(value = "id") String id, Model model) {
        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            return "no-login";
        }
        if(!facebookuserService.isAdminOfPage(id,user)){
            return "no-rights";
        }
        List<Facebookpost> posts = postRepository.findAllByFacebookpageFbIdAndPosted(id,false);
        log.info("Delete-Unposted. Posts to delete size:" + posts.size());
        posts.forEach((post) -> {
            scheduleService.cancelScheduling(post);
            postRepository.delete(post);
        });
        model.addAttribute("page", pageRepository.findByFbId(id));
        model.addAttribute("postList", new ArrayList<>());
        return "page";
    }


    @PostMapping(value = "/schedule/{id}/deleteall")
    public String deleteAll(@PathVariable(value = "id") String id, Model model) {
        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            return "no-login";
        }
        if(!facebookuserService.isAdminOfPage(id,user)){
            return "no-rights";
        }
        List<Facebookpost> posts = postRepository.findAllByFacebookpageFbId(id);
        log.info("Delete-all. Delete size:" + posts.size());
        posts.forEach((post) -> {
            scheduleService.cancelScheduling(post);
            postRepository.delete(post);
        });
        model.addAttribute("page", pageRepository.findByFbId(id));
        model.addAttribute("postList", new ArrayList<>());
        return "page";
    }


    @PostMapping(value = "/schedule/{id}/deleteerror")
    public String deleteError(@PathVariable(value = "id") String id, Model model) {
        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            return "no-login";
        }
        if(!facebookuserService.isAdminOfPage(id,user)){
            return "no-rights";
        }
        List<Facebookpost> posts = postRepository.findAllByFacebookpageFbIdAndError(id,true);
        log.info("Delete-Error: Posts to delete size: " + posts.size());
        posts.forEach((post) -> {
            scheduleService.cancelScheduling(post);
            postRepository.delete(post);
        });
        model.addAttribute("page", pageRepository.findByFbId(id));
        model.addAttribute("postList", new ArrayList<>());
        return "page";
    }


}
