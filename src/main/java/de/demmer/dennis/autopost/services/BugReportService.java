package de.demmer.dennis.autopost.services;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * Service for reporting a bug via email.
 *
 */
@Log4j2
@Service
public class BugReportService{

    @Autowired
    JavaMailSender sender;

    @Value("${debug.mail}")
    String debugMail;

    /**
     * Sends an email with predefined debug text to the specified debugMail in the application.porperties file
     * @param firstName
     * @param lastName
     * @param messageText
     * @param replyEmailAdress
     * @param bugType
     * @param url
     */
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
