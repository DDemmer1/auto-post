package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.UserRepository;
import de.demmer.dennis.autopost.service.SessionService;
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
    SessionService sessionService;

    @Autowired
    PageRepository pageRepository;


    @GetMapping(value = "/schedule/{id}")
    public String schedule(@PathVariable(value = "id") String id, Model model){

        User user = sessionService.getActiveUser();

        model.addAttribute("page",pageRepository.findByFbId(id).getName());

        if(user!=null) model.addAttribute("pageList",user.getPageList());

        return "schedule";
    }

}
