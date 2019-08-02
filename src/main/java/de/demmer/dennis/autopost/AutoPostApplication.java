package de.demmer.dennis.autopost;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/* TODO
    -multiple picture upload
    -show recent posts
    -export posts to tsv
 */

@SpringBootApplication
public class AutoPostApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(AutoPostApplication.class).bannerMode(Banner.Mode.OFF);
    }
}


