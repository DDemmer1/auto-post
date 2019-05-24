package de.demmer.dennis.autopost.services;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.PostDto;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PostService {


    @Autowired
    SessionService sessionService;

    @Autowired
    PageRepository pageRepository;

    @Autowired
    PostRepository postRepository;


    public Post updatePost(Post post, PostDto postDto, String pageFbId){
        if(postDto.getLongitude()!= null && !postDto.getLongitude().isEmpty()) post.setLongitude(Float.parseFloat(postDto.getLongitude()));
        if(postDto.getLongitude()!= null && !postDto.getLatitude().isEmpty()) post.setLatitude(Float.parseFloat(postDto.getLatitude()));

        post.setEnabled(postDto.isEnabled());
        post.setPosted(false);
        post.setError(false);

        post.setContent(postDto.getContent());
        post.setImg(postDto.getImg());
        post.setPageID(pageFbId);
        post.setUser(sessionService.getActiveUser());
        post.setDate(postDto.getDate());
        post.setTime(postDto.getTime());

        post.setPage(pageRepository.findByFbId(pageFbId));

        postRepository.save(post);

        return post;
    }

}
