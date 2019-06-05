package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.repositories.FacebookuserRepository;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.concurrent.ScheduledFuture;


/**
 * Controlls the administration page /admin which is only visible for a logged in admin
 */

@Log4j2
@Controller
public class DebugController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookpostRepository facebookpostRepository;

    @Autowired
    FacebookuserRepository facebookuserRepository;

    @Autowired
    FacebookpageRepository facebookpageRepository;

    @Value("${admin.fb.id}")
    String adminID;

    @Value("${spring.datasource.url}")
    String dbURL;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    String dbtype;

    @Value("${debug.mail}")
    String debugMail;

    @Value("${facebook.redirect.url}")
    String facebookRedirect;



    @GetMapping("/admin")
    public String tasks(Model model) {

        final int numPostsToShow = 200;

        Facebookuser activeUser = sessionService.getActiveUser();

        if (!activeUser.getFbId().equals(adminID)) return "error";


        Map<Integer, ScheduledFuture<?>> tasks = scheduleService.getTasks();

        List<Facebookpost> scheduledTasks = new ArrayList<>();

        int counter = 0;
        for (Map.Entry<Integer, ScheduledFuture<?>> entry : tasks.entrySet()) {
            if(counter > numPostsToShow) break;
            Optional<Facebookpost> postOptional = facebookpostRepository.findById(entry.getKey());
            Facebookpost post = postOptional.get();
            scheduledTasks.add(post);
            counter++;
        }

        Collections.sort(scheduledTasks);


        model.addAttribute("users",facebookuserRepository.count());
        model.addAttribute("pages",facebookpageRepository.count());
        model.addAttribute("posts", facebookpostRepository.count());
        model.addAttribute("posted",facebookpostRepository.findByPosted(true).size());
        model.addAttribute("scheduled",facebookpostRepository.findByScheduled(true).size());
        model.addAttribute("error",facebookpostRepository.findByError(true).size());
        model.addAttribute("enabled",facebookpostRepository.findByEnabled(true).size());
        model.addAttribute("tasks",scheduledTasks.size());

        model.addAttribute("adminmail", debugMail);
        model.addAttribute("admin", facebookuserRepository.findFacebookuserByFbId(adminID).getName());
        model.addAttribute("facebooklogin", facebookRedirect);
        model.addAttribute("dburl", dbURL);
        model.addAttribute("dbtype", dbtype);



        model.addAttribute("dbsize",facebookuserRepository.count() + facebookpageRepository.count() + facebookpostRepository.count());


        model.addAttribute("pageList", activeUser.getPageList());

        int postsToShow = scheduledTasks.size() > numPostsToShow ? numPostsToShow  : scheduledTasks.size();
        model.addAttribute("postList", scheduledTasks.subList(0,postsToShow));

        return "admin";
    }



    /*
     *Mapped by the "Reschedule all" button on the admin page
     */
    @PostMapping("/reschedule")
    public String rescheduleAll(){

        Facebookuser activeUser = sessionService.getActiveUser();

        if (!activeUser.getFbId().equals(adminID)) return "error";

        scheduleService.scheduleAll();

        return "redirect:/admin";
    }


    /*
     *Mapped by the "Kill all tasks" button on the admin page
     */
    @PostMapping("/killall")
    public String killall(){

        Facebookuser activeUser = sessionService.getActiveUser();

        if (!activeUser.getFbId().equals(adminID)) return "error";

        scheduleService.killAllTasks();

        return "redirect:/admin";
    }


}
