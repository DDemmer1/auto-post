package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.PostDto;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.facebook.FacebookSpringSocialService;
import de.demmer.dennis.autopost.services.PostUtilService;
import de.demmer.dennis.autopost.services.image.ImageStorageService;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.userhandling.FacebookuserService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    FacebookSpringSocialService facebookService;

    @Autowired
    PostUtilService postUtilService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    FacebookuserService facebookuserService;


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

        if (user == null) {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        }
        if (!facebookuserService.isAdminOfPage(pageFbId, user)) {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            model.addAttribute("pageList", user.getPageList());
            return "no-rights";
        }

        Facebookpost post = postRepository.findById(Integer.valueOf(postId).intValue());
        Facebookpage page = post.getFacebookpage();
        model.addAttribute("pageList", user.getPageList());
        model.addAttribute("post", post);
        model.addAttribute("page", page);

        ModelMapper modelMapper = new ModelMapper();
        PostDto postDto = new PostDto();
        modelMapper.map(post, postDto);
        model.addAttribute("postDto", postDto);
        model.addAttribute("timeString", postDto.getDate());
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
            Facebookpage page = pageRepository.findByFbIdAndFacebookuser_Id(pageFbId, user.getId());
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
     *
     * @param pageFbId
     * @param postDto
     * @return
     */
    @PostMapping(value = "/schedule/{pageFbId}/new")
    public String saveNewPost(@PathVariable(value = "pageFbId") String pageFbId, @ModelAttribute PostDto postDto, @RequestParam(value = "file", required = false) MultipartFile file) {

        Facebookpost post = postUtilService.updatePost(new Facebookpost(), postDto, pageFbId, file);
        if (post.isEnabled())
            scheduleService.schedulePost(post);


        return "redirect:/schedule/" + pageFbId;
    }

    /**
     * Mapped by the 'save' button on the template for an edited post
     *
     * @param pageFbId
     * @param postDto
     * @return
     */
    @PostMapping(value = "/schedule/{pageFbId}/{postId}")
    public String saveEditedPost(Model model, @PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") String postId, @ModelAttribute PostDto postDto, @RequestParam(value = "file", required = false) MultipartFile file) {
        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            return "no-login";
        }

        if(!facebookuserService.isAdminOfPage(pageFbId,user)){
            return "no-rights";
        }
        Facebookpost post = postRepository.findById(Integer.valueOf(postId).intValue());
        scheduleService.cancelScheduling(post);
        Facebookpost updatedPost = postUtilService.updatePost(post, postDto, pageFbId, file);
        if (updatedPost.isEnabled())
            scheduleService.schedulePost(updatedPost);

        return "redirect:/schedule/" + pageFbId;
    }


    /**
     * Mapped by the 'delete' button on the 'post' template
     *
     * @param pageFbId
     * @return
     */
    @GetMapping(value = "/schedule/{pageFbId}/{postId}/delete")
    public String deletePost(@PathVariable(value = "pageFbId") String pageFbId, @PathVariable(value = "postId") Integer postId) {
        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            return "no-login";
        }

        if(!facebookuserService.isAdminOfPage(pageFbId,user)){
            return "no-rights";
        }
        Facebookpost post = postRepository.findById(postId.intValue());
        scheduleService.cancelScheduling(post);

        postRepository.deleteByIdAndFacebookpageFbId(postId, pageFbId);

        return "redirect:/schedule/" + pageFbId;
    }


}
