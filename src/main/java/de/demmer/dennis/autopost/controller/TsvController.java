package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.facebook.FacebookSpringSocialService;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.tsvimport.MalformedTsvException;
import de.demmer.dennis.autopost.services.tsvimport.TsvService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Handles requests for the 'tsvform' template
 */
@Log4j2
@Controller
public class TsvController {

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookpageRepository pageRepository;

    @Autowired
    TsvService tsvService;

    @Autowired
    FacebookSpringSocialService facebookService;

    @Autowired
    FacebookpostRepository postRepository;

    @Autowired
    ScheduleService scheduleService;

    /**
     * Mapped by the file picker in the 'tsvform' template
     *
     * @param id
     * @param multiFile
     * @param modelMap
     * @param imgcheck
     * @return
     */
    @PostMapping(value = "/schedule/{id}/tsvform/upload", consumes = {"multipart/form-data"})
    public ModelAndView uploadTSV(@PathVariable(value = "id") String id, @RequestParam("file") MultipartFile multiFile, ModelMap modelMap, @RequestParam(name = "imgcheck", defaultValue = "off") String imgcheck, @RequestParam(name = "datecheck", defaultValue = "off") String datecheck) {

        Facebookuser user = sessionService.getActiveUser();
        if (user == null) {
            modelMap.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return new ModelAndView("redirect:/error", modelMap);
        }
        modelMap.addAttribute("page", pageRepository.findByFbIdAndFacebookuser_Id(id, user.getId()));

        //Add fbuser data from session to view
        List<Facebookpost> posts = new ArrayList<>();
        for (Facebookpage page : pageRepository.findAllByFbId(id)) {
            posts.addAll(page.getFacebookposts());
        }

        Collections.sort(posts);
        modelMap.addAttribute("pageList", user.getPageList());
        modelMap.addAttribute("postList", posts);

        //No Input
        if (multiFile == null || multiFile.isEmpty() || multiFile.getName().equals("")) {
            return new ModelAndView("redirect:/schedule/" + id + "/tsvform", modelMap);
        }

        //MultipartFile to File
        File file = null;
        try {
            file = new File(multiFile.getOriginalFilename());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multiFile.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //parse tsv to posts
            List<Facebookpost> tsvPosts = tsvService.parseTSV(file, id, imgcheck.equals("on"), datecheck.equals("on"));
            for (Facebookpost post : tsvPosts) {
                postRepository.save(post);
                scheduleService.schedulePost(post);
            }

            //delete temporary tsv file
            file.delete();

            //get the number of errors
            int numErrors = 0;
            for (Facebookpost post : tsvPosts) {
                if (post.isError()) {
                    numErrors++;
                }
            }

            //add number of added posts and trigger "android toast" like pop up
            modelMap.addAttribute("tsvSuccess", true);
            modelMap.addAttribute("numAddedPosts", tsvPosts.size());
            modelMap.addAttribute("numErrors", numErrors);

        } catch (MalformedTsvException e) { // Handle malformed files
            modelMap.addAttribute("line", e.getRow());
            modelMap.addAttribute("linecontent", e.getContent());
            modelMap.addAttribute("message", e.getMessage());
            modelMap.addAttribute("error", true);
            file.delete();
            return new ModelAndView("redirect:/schedule/" + id + "/tsvform", modelMap);
        }
        return new ModelAndView("redirect:/schedule/" + id, modelMap);
    }


    /**
     * Mappgin for the template 'tsvform'
     *
     * @param id
     * @param model
     * @param tsvSuccess
     * @param numAddedPosts
     * @return
     */
    @GetMapping(value = "/schedule/{id}/tsvform")
    public String tsvForm(@PathVariable(value = "id") String id, Model model, @RequestParam(value = "tsvSuccess", required = false) Boolean tsvSuccess, @RequestParam(value = "numAddedPosts", required = false) Integer numAddedPosts) {

        Facebookuser user = sessionService.getActiveUser();

        model.addAttribute("page", pageRepository.findByFbIdAndFacebookuser_Id(id, user.getId()));

        if (user != null) {
            model.addAttribute("pageList", user.getPageList());
        } else {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        }


        model.addAttribute("numAddedPosts", numAddedPosts);
        model.addAttribute("tsvSuccess", tsvSuccess);


        return "tsvform";
    }


}
