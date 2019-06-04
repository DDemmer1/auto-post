package de.demmer.dennis.autopost.services;

import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.PostDto;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Utility class for @{@link Facebookpost}
 *
 */
@Log4j2
@Service
public class PostUtilService {


    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookpageRepository pageRepository;

    @Autowired
    FacebookpostRepository postRepository;


    /**
     * Updates the atributes of a @{@link Facebookpost} via a @{@link PostDto}
     *
     * @param post
     * @param postDto
     * @param pageFbId
     * @return
     */
    public Facebookpost updatePost(Facebookpost post, PostDto postDto, String pageFbId){
        if(postDto.getLongitude()!= null && !postDto.getLongitude().isEmpty()) post.setLongitude(Float.parseFloat(postDto.getLongitude()));
        if(postDto.getLongitude()!= null && !postDto.getLatitude().isEmpty()) post.setLatitude(Float.parseFloat(postDto.getLatitude()));

        post.setEnabled(postDto.isEnabled());
        post.setPosted(false);
        post.setError(false);

        post.setContent(postDto.getContent());
        post.setImg(postDto.getImg());
        post.setPageID(pageFbId);
        post.setFacebookuser(sessionService.getActiveUser());
        post.setDate(postDto.getDate());
        post.setTime(postDto.getTime());

        post.setFacebookpage(pageRepository.findByFbId(pageFbId));

        postRepository.save(post);


        return post;
    }

}
