package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.PostDto;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.PostService;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Transactional
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

    @Autowired
    PostService postService;

    @Autowired
    ScheduleService scheduleService;


    @GetMapping(value = "/schedule/{pageFbId}/{postId}")
    public String editExistingPost(@PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") String postId, Model model) {

            Facebookuser user = sessionService.getActiveUser();

            if (user != null) {
                Facebookpage page = pageRepository.findByFbId(pageFbId);
                Facebookpost post = postRepository.findByIdAndUserId(Integer.valueOf(postId), user.getId());
                model.addAttribute("pageList", user.getPageList());
                model.addAttribute("post", post);
                model.addAttribute("page", page);

                ModelMapper modelMapper = new ModelMapper();
                PostDto postDto = new PostDto();
                modelMapper.map(post,postDto);
                model.addAttribute("postDto", postDto);
                model.addAttribute("timeString",postDto.getDate());
            } else {
                model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
                return "no-login";
            }


        return "post";
    }


    @GetMapping(value = "/schedule/{pageFbId}/new")
    public String editNewPost(Model model, @PathVariable(value = "pageFbId") String pageFbId) {
        Facebookuser user = sessionService.getActiveUser();

        if (user != null) {
            Facebookpage page = pageRepository.findByFbId(pageFbId);
            model.addAttribute("pageList", user.getPageList());
            model.addAttribute("page", page);
            model.addAttribute("postDto", new PostDto());

        } else {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        }

        return "post";
    }


    @PostMapping(value = "/schedule/{pageFbId}/new")
    public String saveNewPost(@PathVariable(value = "pageFbId") String pageFbId, @ModelAttribute PostDto postDto) {

        Facebookpost post = postService.updatePost(new Facebookpost(), postDto, pageFbId);
        if(post.isEnabled())
        scheduleService.schedulePost(post);

        return "redirect:/schedule/" + pageFbId;
    }


    @PostMapping(value = "/schedule/{pageFbId}/{postId}")
    public String saveEditedPost(Model model, @PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") String postId, @ModelAttribute PostDto postDto) {

        Facebookpost post = postRepository.findByIdAndUserId(Integer.valueOf(postId),sessionService.getActiveUser().getId());
        scheduleService.cancelScheduling(post);
        Facebookpost updatedPost = postService.updatePost(post,postDto,pageFbId);
        if(updatedPost.isEnabled())
        scheduleService.schedulePost(updatedPost);

        return "redirect:/schedule/" + pageFbId;
    }



    @GetMapping(value = "/schedule/{pageFbId}/{postId}/delete")
    public String deletePost(@PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") Integer postId) {

        Facebookpost post = postRepository.findByIdAndPageFbId(postId, pageFbId);
        scheduleService.cancelScheduling(post);

        postRepository.deleteByIdAndPageFbId(postId, pageFbId);

        return "redirect:/schedule/" + pageFbId;
    }







}
