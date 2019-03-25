package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.properties.TestContext;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Log4j2
public class PostDtoTests  {

    @Test
    public void testTimeConversion(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        try {
            date1 = dateFormat.parse("2018-12-11" + " " + "11:10");
            date2 = dateFormat.parse("2018-12-11" + " " + "11:00");
            date3 = dateFormat.parse("2017-11-11" + " " + "11:20");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Date> dates = new ArrayList<>();
        dates.add(date1);
        dates.add(date2);
        dates.add(date3);


        Collections.sort(dates, Collections.reverseOrder());
        System.out.println(dates.toString());


    }

}
