package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.tsvimport.MalformedTsvException;
import de.demmer.dennis.autopost.services.tsvimport.TsvService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
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
import java.util.Collections;
import java.util.List;


@Controller
public class TsvController {

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookpageRepository pageRepository;

    @Autowired
    TsvService tsvService;

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookpostRepository postRepository;

    @Autowired
    ScheduleService scheduleService;

    @PostMapping(value="/schedule/{id}/tsvform/upload")
    public ModelAndView uploadTSV(@PathVariable(value = "id") String id, @RequestParam("file") MultipartFile multiFile, ModelMap modelMap){

        Facebookuser user = sessionService.getActiveUser();

        modelMap.addAttribute("page", pageRepository.findByFbId(id));

        //Add fbuser data from session
        if (user != null) {
            List<Facebookpost> posts = pageRepository.findByFbId(id).getFacebookposts();
            Collections.sort(posts);
            modelMap.addAttribute("pageList", user.getPageList());
            modelMap.addAttribute("postList", posts);
        } else {
            modelMap.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return new ModelAndView("redirect:/error", modelMap);
        }

        //No Input
        if(multiFile ==null || multiFile.isEmpty() || multiFile.getName().equals("")){
            return new ModelAndView("redirect:/schedule/" + id +"/tsvform", modelMap);
        }

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
            List<Facebookpost> tsvPosts = tsvService.parseTSV(file, id);
            for (Facebookpost post : tsvPosts) {
                postRepository.save(post);
                scheduleService.schedulePost(post);
            }

            file.delete();

            modelMap.addAttribute("tsvSuccess" , true);
            modelMap.addAttribute("numAddedPosts" , tsvPosts.size());

        } catch (MalformedTsvException e) {
//            e.printStackTrace();
            modelMap.addAttribute("line",e.getRow());
            modelMap.addAttribute("linecontent",e.getContent());
            modelMap.addAttribute("message",e.getMessage());
            modelMap.addAttribute("error",true);
            file.delete();
            return new ModelAndView("redirect:/schedule/" + id +"/tsvform", modelMap);
        }
        return new ModelAndView("redirect:/schedule/"+ id, modelMap);
    }



    @GetMapping(value="/schedule/{id}/tsvform")
    public String tsvForm(@PathVariable(value = "id") String id, Model model, @RequestParam(value = "tsvSuccess", required = false) Boolean tsvSuccess, @RequestParam(value = "numAddedPosts", required = false) Integer numAddedPosts){

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


        model.addAttribute("numAddedPosts",numAddedPosts);
        model.addAttribute("tsvSuccess",tsvSuccess);


        return "tsvform";
    }


}
