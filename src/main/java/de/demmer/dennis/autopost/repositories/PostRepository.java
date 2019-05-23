package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findByPageIdAndUserId(int pageID, int userId);

    List<Post> deleteByPageIdAndUserId(int pageID, int userId);

    void deleteAllByUserIdAndPageId(int userId, int pageId);

    void deleteByIdAndPageFbId(int postId, String pageFbId);

    Post findByIdAndUserId(int postId, int userId);

    Post findByIdAndPageFbId(int postId, String pageFbId);

    List<Post> findByUserIdOrderByDateAsc(int userID);

}
