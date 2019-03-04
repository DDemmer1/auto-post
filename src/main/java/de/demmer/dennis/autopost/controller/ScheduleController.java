package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Transactional
@Log4j2
@Controller
public class ScheduleController {

    @Autowired
    PageRepository pageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpSession session;


    @GetMapping(value = "/schedule/{id}")
    public String schedule(@PathVariable(value = "id") String id, Model model){

        Map<String,String> userMap = (Map<String, String>) session.getAttribute("activeuser");
        String fbid = userMap.get("fbid");
        User user = userRepository.findUserByFbId(fbid);

        model.addAttribute("page",id);
        model.addAttribute("pageList",pageRepository.findByUserId(user.getId()));




        return "schedule";
    }

}
