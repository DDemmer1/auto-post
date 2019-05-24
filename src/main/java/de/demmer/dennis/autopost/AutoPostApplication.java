package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.Transactional;




/*
 * Uncomment following lines for development
 * Please change the redirect URL in application.properties to your development domain
 */

@Transactional (rollbackFor = UserException.class)
@SpringBootApplication
public class AutoPostApplication implements CommandLineRunner{

    public static void main(String[] args){SpringApplication.run(AutoPostApplication.class, args);}

    @Override
    public void run(String... args) {

    }

//****************************************************************************************************

/*
 * Uncomment following lines for production deployment with Jetty 9
 * Please change the redirect URL in application.properties to your deployment domain
 */

//@Transactional
//@SpringBootApplication
//
//public class AutoPostApplication extends SpringBootServletInitializer {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return configureApplication(builder);
//    }
//
//    public static void main(String[] args) {
//        configureApplication(new SpringApplicationBuilder()).run(args);
//    }
//
//    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
//        return builder.sources(AutoPostApplication.class).bannerMode(Banner.Mode.OFF);
//    }


}


