package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.Facebookpage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface FacebookpageRepository extends CrudRepository<Facebookpage, Integer> {

    List<Facebookpage> findByFacebookuserId(int userID);

    Facebookpage findByFbId(String fbId);
    Facebookpage findByFbIdAndFacebookuser_Id(String fbId, int id);

    void deleteByFbId(String fbId);


}