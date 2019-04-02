package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.UserRepository;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Log4j2
@SpringBootApplication
public class AutoPostApplication implements CommandLineRunner{


    public static void main(String[] args){SpringApplication.run(AutoPostApplication.class, args);}


    @Override
    public void run(String... args) throws Exception {

    }
}


