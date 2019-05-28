package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.properties.TestContext;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTests extends TestContext{


    @Autowired
    UserRepository userRepository;

    @Test
    public void findAllTest(){
        log.info(userRepository.findAll());
    }



    @Test
    public void generateTestTsv(){

        Date date = new Date();
        int hour = date.getHours();
        int minutes = date.getMinutes();

        int year = date.getYear();
        int monthInt = date.getMonth() +1;
        String month = monthInt < 10 ? "0" +monthInt : monthInt +"";
        int day = date.getDay();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.format(date);

        for (int i = 0; i < 10; i++) {
            minutes = minutes +5;


            System.out.println(dateFormat.format(date)+"\t" + hour+":" + minutes + "\tDer 25. Mai in der Weltliteratur - Ludwig Ganghofer: Edelweißkönig. #tiwoli https://tiwoli.spinfo.uni-koeln.de/view?id=362&lang=de\thttps://tiwoli.spinfo.uni-koeln.de/image/flashcard?id=362&lang=de\t\t");
        }
    }

}
