package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.PostDto;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.PostUtilService;
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
    FacebookpageRepository pageRepository;

    @Autowired
    FacebookpostRepository postRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookService facebookService;

    @Autowired
    PostUtilService postUtilService;

    @Autowired
    ScheduleService scheduleService;


    /**
     * Called when clicked on a post in the 'page' template. Triggers an editing template
     *
     * @param pageFbId
     * @param postId
     * @param model
     * @return
     */
    @GetMapping(value = "/schedule/{pageFbId}/{postId}")
    public String editExistingPost(@PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") String postId, Model model) {

            Facebookuser user = sessionService.getActiveUser();

            if (user != null) {
                Facebookpage page = pageRepository.findByFbId(pageFbId);
                Facebookpost post = postRepository.findByIdAndFacebookuserId(Integer.valueOf(postId), user.getId());
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

    /**
     * Mapped by the "Add new Post" button on the 'page' template
     *
     * @param model
     * @param pageFbId
     * @return
     */
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


    /**
     * Mapped by the 'save' button on the template for a new post
     * @param pageFbId
     * @param postDto
     * @return
     */
    @PostMapping(value = "/schedule/{pageFbId}/new")
    public String saveNewPost(@PathVariable(value = "pageFbId") String pageFbId, @ModelAttribute PostDto postDto) {

        Facebookpost post = postUtilService.updatePost(new Facebookpost(), postDto, pageFbId);
        if(post.isEnabled())
        scheduleService.schedulePost(post);

        return "redirect:/schedule/" + pageFbId;
    }

    /**
     * Mapped by the 'save' button on the template for an edited post
     * @param pageFbId
     * @param postDto
     * @return
     */
    @PostMapping(value = "/schedule/{pageFbId}/{postId}")
    public String saveEditedPost(Model model, @PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") String postId, @ModelAttribute PostDto postDto) {

        Facebookpost post = postRepository.findByIdAndFacebookuserId(Integer.valueOf(postId),sessionService.getActiveUser().getId());
        scheduleService.cancelScheduling(post);
        Facebookpost updatedPost = postUtilService.updatePost(post,postDto,pageFbId);
        if(updatedPost.isEnabled())
        scheduleService.schedulePost(updatedPost);

        return "redirect:/schedule/" + pageFbId;
    }


    /**
     * Mapped by the 'delete' button on the 'post' template
     * @param pageFbId
     * @return
     */
    @GetMapping(value = "/schedule/{pageFbId}/{postId}/delete")
    public String deletePost(@PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") Integer postId) {

        Facebookpost post = postRepository.findByIdAndFacebookpageFbId(postId, pageFbId);
        scheduleService.cancelScheduling(post);

        postRepository.deleteByIdAndFacebookpageFbId(postId, pageFbId);

        return "redirect:/schedule/" + pageFbId;
    }







}
