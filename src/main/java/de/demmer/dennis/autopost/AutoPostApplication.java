package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.User;
import de.demmer.dennis.autopost.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication
public class AutoPostApplication implements CommandLineRunner {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository repository;


    public static void main(String[] args) {
        SpringApplication.run(AutoPostApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        repository.save(new User("id123","token123","secret123"));

//        logger.info("All users  -> {}", repository.findAll());

    }




}


