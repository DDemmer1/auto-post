package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.Facebookpage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface PageRepository extends CrudRepository<Facebookpage, Integer> {

    List<Facebookpage> findByUserId(int userID);

    Facebookpage findByIdAndUserId(int pageId, int userId);

    Facebookpage findByFbId(String fbId);

    void deleteByUserId(int userId);

    void deleteByFbId(String fbId);


}