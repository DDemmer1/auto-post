package de.demmer.dennis.autopost.service;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Log4j2
@Service
public class BugReportService{

    @Autowired
    JavaMailSender sender;

    @Value("${debug.mail}")
    String debugMail;

    @SneakyThrows
    public void sendMail(String firstName, String lastName, String messageText, String replyEmailAdress, String bugType, String url){
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.addTo(debugMail);
        helper.setFrom("bug@autopost.de");
        if(bugType.isEmpty()){
            bugType ="Unspecific Bug";
        }
        helper.setSubject(bugType +" - reported by: '" + replyEmailAdress+"'");
        helper.setText("Hi, you have a new bug report\n\nMessage:\n" + messageText+"\n\nError at URL: " + url + "\nReply to: " + replyEmailAdress + "\n\nBest regards\n" + firstName + " " + lastName);
        sender.send(message);
    }

}
