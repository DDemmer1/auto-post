package de.demmer.dennis.autopost.services.tsvimport;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.services.scheduling.DateParser;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Log4j2
@Service
public class TsvService {

    @Autowired
    FacebookpageRepository pageRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    ScheduleService scheduleService;


    public List<Facebookpost> parseTSV(File tsvFile, String id, boolean imgcheck) throws MalformedTsvException {


        TsvParserSettings settings = new TsvParserSettings();

        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);

        List<String[]> allRows = parser.parseAll(tsvFile);

//        allRows.forEach(row -> System.out.println(Arrays.asList(row).toString()));

        List<Facebookpost> parsedPosts = new ArrayList<>();

        int i = 1;
        for (String[] row : allRows) {
            if(row[0].startsWith("//")){
                log.info("Comment in line " + i);
                continue;
            }

            Facebookpost post = arrayToPost(row, i, imgcheck);
            if (post != null) {
                try {
                    if (post.getContent().equals("") && post.getImg().equals("")) {
                        throw new MalformedTsvException("Content Error", i, "no content or image detected");
                    }
                } catch (NullPointerException ne) {
                    throw new MalformedTsvException("Content Error", i, "no content or image detected");
                }
                Facebookpage page = pageRepository.findByFbId(id);
                post.setFacebookpage(page);
                post.setPageID(page.getFbId());
                post.setEnabled(true);
                post.setFacebookuser(sessionService.getActiveUser());
                parsedPosts.add(post);
                i++;
            } else throw new MalformedTsvException("Unspecific Formatting Error", i, Arrays.asList(row).toString());
        }


        return parsedPosts;
    }

    private Facebookpost arrayToPost(String[] posts, int row, boolean imgcheck) throws MalformedTsvException {

        Facebookpost post = new Facebookpost();
        for (int col = 0; col < posts.length; col++) {
            String value = posts[col];
            String originalDate = "";
            switch (col) {
                case 0:
                    //Date
                    try {
                        originalDate = value;
                        String date = DateParser.parse(value);
                        post.setDate(date);
                        if(date==null){
                            throw new MalformedTsvException("Date Error", row, value);
                        }
                    } catch (Exception e) {
                        throw new MalformedTsvException("Date Error", row, value);
                    }
                    break;
                case 1:
                    //Time
                    try {
                        post.setTime(value);
                        int delay = scheduleService.getDelay(post);
                        if (delay < 0) {
                            throw new MalformedTsvException("Time Error", row, originalDate + " " + value);
                        }
                    } catch (Exception e) {
                        throw new MalformedTsvException("Time Error", row, originalDate + " " + value);
                    }
                    break;
                case 2:
                    //Content
                    post.setContent(value != null ? value : "");
                    break;
                case 3:
                    //Image
                    if(imgcheck){
                        if (!isImage(value)) throw new MalformedTsvException("Image Error", row, value);
                    }
                    post.setImg(value);
                    break;
                default:
                    break;

            }
        }

        return post;
    }


    private boolean isImage(String imagePath) {
        try {
            URL url = new URL(imagePath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            int status = con.getResponseCode();

            if (status != 200) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
