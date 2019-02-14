package de.demmer.dennis.autopost.controller;


import de.demmer.dennis.autopost.entities.User;
import de.demmer.dennis.autopost.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Transactional
@Controller
public class DevController {

    @Autowired
    UserRepository userRepository;


    @GetMapping(value="/")
    public String devLogin(Model model){

        User testUser = userRepository.findUserById(1);


        model.addAttribute("user",testUser);

        return "login";
    }
}
