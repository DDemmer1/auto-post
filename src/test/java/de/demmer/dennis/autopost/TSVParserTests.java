package de.demmer.dennis.autopost;


import de.demmer.dennis.autopost.services.tsvimport.MalformedTsvException;

import de.demmer.dennis.autopost.services.tsvimport.TsvService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class TSVParserTests {


    @Test
    public void testTSVParse(){

        try {
            new TsvService().parseTSV(new File("C:\\Users\\IDH\\IdeaProjects\\auto-post\\src\\test\\java\\de\\demmer\\dennis\\autopost\\tsvTest"), "55", true, true);
        } catch (MalformedTsvException e) {
            e.printStackTrace();
        }

    }


}
