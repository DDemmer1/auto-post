package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findByGroupIdAndUserId(int groupID, int userId);

    List<Post> deleteByGroupIdAndUserId(int groupID, int userId);

    void deleteAllByUserIdAndGroupId(int userId, int groupId);

    Post findByIdAndUserId(int postId, int userId);

    List<Post> findByUserIdOrderByDateAsc(int userID);

}
