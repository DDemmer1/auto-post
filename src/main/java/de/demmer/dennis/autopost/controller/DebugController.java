package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RestController
public class DebugController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    SessionService sessionService;

    @Value("${admin.fb.id}")
    String adminID;


    @GetMapping("/tasks")
    public String tasks() {

        if(!sessionService.getActiveUser().getFbId().equals(adminID))return "";

        Map<Integer, ScheduledFuture<?>> tasks = scheduleService.getTasks();

        StringBuffer buffer = new StringBuffer();

        tasks.entrySet().forEach((entry) -> {
            ScheduledFuture<?> future = entry.getValue();
            buffer.append("Delay (in minutes): " + future.getDelay(TimeUnit.MINUTES) + ", PostId: "+ entry.getKey() +  "<br>");
        });

        return buffer.toString();
    }
}
