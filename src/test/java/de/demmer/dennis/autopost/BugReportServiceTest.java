package de.demmer.dennis.autopost;


import de.demmer.dennis.autopost.service.BugReportService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class BugReportServiceTest {


    @Autowired
    BugReportService bugReportService;

    @Test
    public void sentMailTest(){

        bugReportService.sendMail("Dennis","Demmer","Application failed to load contacts","Dennis.Demmer@uni-koeln.de", "Bug Report Test", "https://localhost:8080/contact");
    }

}
