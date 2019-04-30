package de.demmer.dennis.autopost.services.tsvimport;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.repositories.PageRepository;
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

        for (String[] row : allRows) {
            Post post = arrayToPost(row);
            Page page = pageRepository.findByFbId(id);
            post.setPage(page);
            post.setEnabled(true);
            post.setUser(sessionService.getActiveUser());
            parsedPosts.add(post);
        }


        return parsedPosts;
    }

    private Post arrayToPost(String[] array) {

        Post post = new Post();
        for (int i = 0; i < array.length; i++) {
            String value = array[i];
            switch (i) {
                case 0:
                    post.setDate(value);
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

            }
        }

        return post;
    }


}
