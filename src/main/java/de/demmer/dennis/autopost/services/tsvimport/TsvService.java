package de.demmer.dennis.autopost.services.tsvimport;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.services.scheduling.DateParser;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
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


    public List<Post> parseTSV(File tsvFile, String id) throws MalformedTsvException {


        TsvParserSettings settings = new TsvParserSettings();

        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);

        List<String[]> allRows = parser.parseAll(tsvFile);

        allRows.forEach(row -> System.out.println(Arrays.asList(row).toString()));


        List<Post> parsedPosts = new ArrayList<>();

        int i = 1;
        for (String[] row : allRows) {
            i++;
            Post post = arrayToPost(row);
            if(post!=null){
                Page page = pageRepository.findByFbId(id);
                post.setPage(page);
                post.setEnabled(true);
                post.setUser(sessionService.getActiveUser());
                parsedPosts.add(post);
            } else throw new MalformedTsvException("Formatting Error in line: "+ i, i);
        }


        return parsedPosts;
    }

    private Post arrayToPost(String[] array) {

        Post post = new Post();
        for (int i = 0; i < array.length; i++) {
            String value = array[i];
            switch (i) {
                case 0:
                    String date = DateParser.parse(value);
                    if(date==null) return null;
                    post.setDate(date);

                    break;
                case 1:
                    post.setTime(value);
                    break;
                case 2:
                    post.setContent(value);
                    break;
                case 3:
                    post.setImg(value);
                    break;
                default:
                    break;

            }
        }

        return post;
    }


}
