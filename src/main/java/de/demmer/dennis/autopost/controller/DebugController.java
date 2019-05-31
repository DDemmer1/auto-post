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


        Facebookuser activeUser = sessionService.getActiveUser();

        if (!activeUser.getFbId().equals(adminID)) return "error";


        Map<Integer, ScheduledFuture<?>> tasks = scheduleService.getTasks();

        List<Facebookpost> scheduledTasks = new ArrayList<>();

        for (Map.Entry<Integer, ScheduledFuture<?>> entry : tasks.entrySet()) {
            Optional<Facebookpost> postOptional = facebookpostRepository.findById(entry.getKey());
            Facebookpost post = postOptional.get();
            scheduledTasks.add(post);
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
        model.addAttribute("postList", scheduledTasks);

        return "admin";
    }



    @PostMapping("/reschedule")
    public String rescheduleAll(){

        Facebookuser activeUser = sessionService.getActiveUser();

        if (!activeUser.getFbId().equals(adminID)) return "error";

        scheduleService.scheduleAll();

        return "redirect:/admin";
    }



    @PostMapping("/killall")
    public String killall(){

        Facebookuser activeUser = sessionService.getActiveUser();

        if (!activeUser.getFbId().equals(adminID)) return "error";

        scheduleService.killAllTasks();

        return "redirect:/admin";
    }


}
