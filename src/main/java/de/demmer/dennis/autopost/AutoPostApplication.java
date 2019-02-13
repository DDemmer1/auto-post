package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.User;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.TreeMap;


@Log4j2
@SpringBootApplication
public class AutoPostApplication implements CommandLineRunner{

    @Autowired
    UserRepository repository;


    public static void main(String[] args){SpringApplication.run(AutoPostApplication.class, args);}


    @Override
    public void run(String... args) throws Exception {
//        User user = new User("id", "oauth", "name", "mail");
//        repository.save(user);
//        log.info("All users  -> {}", repository.findAll());
    }
}


