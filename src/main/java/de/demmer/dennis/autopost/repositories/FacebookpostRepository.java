package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.Facebookpost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FacebookpostRepository extends CrudRepository<Facebookpost, Integer> {


    void deleteByIdAndFacebookpageFbId(int postId, String pageFbId);

    Facebookpost findByIdAndFacebookuserId(int postId, int userId);

    Facebookpost findByIdAndFacebookpageFbId(int postId, String pageFbId);

    List<Facebookpost> findByScheduledAndFacebookuserId(boolean scheduled, int userId);

    List<Facebookpost> findByPostedAndFacebookuserId(boolean posted, int userId);

    List<Facebookpost> findByEnabledAndFacebookuserId(boolean enabled, int userId);

    List<Facebookpost> findByErrorAndFacebookuserId(boolean error, int userId);

    List<Facebookpost> findByEnabledAndPostedAndFacebookuserId(boolean enabled, boolean posted, int userId);

    List<Facebookpost> findByEnabledAndPostedAndError(boolean enabled, boolean posted, boolean error);

}
