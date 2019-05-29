package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.Facebookpost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Facebookpost, Integer> {

    List<Facebookpost> findByPageIdAndUserId(int pageID, int userId);

    List<Facebookpost> deleteByPageIdAndUserId(int pageID, int userId);

    void deleteAllByUserIdAndPageId(int userId, int pageId);

    void deleteByIdAndPageFbId(int postId, String pageFbId);

    Facebookpost findByIdAndUserId(int postId, int userId);

    Facebookpost findByIdAndPageFbId(int postId, String pageFbId);

    List<Facebookpost> findByUserIdOrderByDateAsc(int userID);

    List<Facebookpost> findByScheduledAndUserId(boolean scheduled, int userId);

    List<Facebookpost> findByPostedAndUserId(boolean posted, int userId);


    List<Facebookpost> findByEnabledAndUserId(boolean enabled, int userId);

    List<Facebookpost> findByErrorAndUserId(boolean error, int userId);

    List<Facebookpost> findByEnabledAndPostedAndUserId (boolean enabled, boolean posted, int userId);

    List<Facebookpost> findByEnabledAndPostedAndError(boolean enabled, boolean posted, boolean error);

}
