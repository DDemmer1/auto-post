package de.demmer.dennis.autopost.configuration;

import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


//@Component
public class ApplicationConfiguration {

//    @Autowired
//    ScheduleService scheduleService;
//
//
//    //Schedule fbposts on application startup
//    @EventListener(ApplicationReadyEvent.class)
//    public void handleContextRefresh() {
//        scheduleService.scheduleAll();
//    }
}
