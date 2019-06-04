package de.demmer.dennis.autopost.services.scheduling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateParser {

    /**
     * Parses a date String to the database date pattern yyyy-MM-dd
     *
     * @param input
     * @return
     */
    public static String parse(String input, boolean datecheck){

        List<String> patternList = new ArrayList<>();
        patternList.add("yyyy-MM-dd");
        patternList.add("yyyy/MM/dd");
        patternList.add("yyyy.MM.dd");
        patternList.add("dd.MM.yyyy");
        patternList.add("dd-MM-yyyy");
        patternList.add("dd/MM/yyyy");

        for (String pattern : patternList ) {
            if(isParsable(input, pattern)){
                try {
                    Date date1 = new SimpleDateFormat(pattern).parse(input);
                    date1.setHours(1);
                    Date current = new Date();
                    current.setHours(0);

                    if(datecheck && !date1.after(current)){
                        return null;
                    } else return new SimpleDateFormat("yyyy-MM-dd").format(date1);



                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    //checks if String is parsable to date pattern
    private static boolean isParsable(String input, String pattern){

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        try {
            format.parse(input);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

}
