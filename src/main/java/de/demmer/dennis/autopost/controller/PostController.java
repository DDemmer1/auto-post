package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.PostDto;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.service.FacebookService;
import de.demmer.dennis.autopost.service.PostService;
import de.demmer.dennis.autopost.service.SessionService;
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


    @GetMapping(value = "/schedule/{pageFbId}/{postId}")
    public String editExistingPost(@PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") String postId, Model model) {

            User user = sessionService.getActiveUser();

            if (user != null) {
                Page page = pageRepository.findByFbId(pageFbId);
                Post post = postRepository.findByIdAndUserId(Integer.valueOf(postId), user.getId());
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
        User user = sessionService.getActiveUser();

        if (user != null) {
            Page page = pageRepository.findByFbId(pageFbId);
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

        postService.updatePost(new Post(), postDto, pageFbId);

        return "redirect:/schedule/" + pageFbId;
    }


    @PostMapping(value = "/schedule/{pageFbId}/{postId}")
    public String saveEditedPost(Model model, @PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") String postId, @ModelAttribute PostDto postDto) {

        Post post = postRepository.findByIdAndUserId(Integer.valueOf(postId),sessionService.getActiveUser().getId());
        postService.updatePost(post,postDto,pageFbId);
        return "redirect:/schedule/" + pageFbId;
    }







}
