package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.services.scheduling.DateParser;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//
//@Log4j2
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DateParserTests {


    @Test
    public void parseTest(){

        String date = DateParser.parse("18.05.2019",true);
        System.out.println(date);
        System.out.println("*******************");

    }
}
