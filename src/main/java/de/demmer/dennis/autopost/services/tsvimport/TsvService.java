package de.demmer.dennis.autopost.services.tsvimport;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.services.PostService;
import de.demmer.dennis.autopost.services.scheduling.DateParser;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Log4j2
@Service
public class TsvService {

    @Autowired
    PageRepository pageRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    ScheduleService scheduleService;


    public List<Post> parseTSV(File tsvFile, String id) throws MalformedTsvException {


        TsvParserSettings settings = new TsvParserSettings();

        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);

        List<String[]> allRows = parser.parseAll(tsvFile);

//        allRows.forEach(row -> System.out.println(Arrays.asList(row).toString()));

        List<Post> parsedPosts = new ArrayList<>();

        int i = 1;
        for (String[] row : allRows) {
            Post post = arrayToPost(row,i);
            if (post != null) {
                Page page = pageRepository.findByFbId(id);
                post.setPage(page);
                post.setPageID(page.getFbId());
                post.setEnabled(true);
                post.setUser(sessionService.getActiveUser());
                parsedPosts.add(post);
                i++;
            } else throw new MalformedTsvException("Unspecific Formatting Error", i, Arrays.asList(row).toString());
        }


        return parsedPosts;
    }

    private Post arrayToPost(String[] posts, int row) throws MalformedTsvException {

        Post post = new Post();
        for (int col = 0; col < posts.length; col++) {
            String value = posts[col];
            String originalDate ="";
            switch (col) {
                case 0:
                    //Date
                    originalDate = value;
                    String date = DateParser.parse(value);
                    if (date == null) {
                        throw new MalformedTsvException("Date Error", row, value);
                    }
                    post.setDate(date);

                    break;
                case 1:
                    //Time
                    post.setTime(value);
                    int delay = scheduleService.getDelay(post);
                    if (delay < 0) {
                        throw new MalformedTsvException("Time Error", row, originalDate + " " +value);
                    }
                    break;
                case 2:
                    //Content
                    post.setContent(value);
                    break;
                case 3:
                    //Image
                    if (!isImage(value)) throw new MalformedTsvException("Image Error", row, value);
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

            if(status!= 200){
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
