package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.Facebookpage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface FacebookpageRepository extends CrudRepository<Facebookpage, Integer> {

    List<Facebookpage> findByFacebookuserId(int userID);

    Facebookpage findByFbId(String fbId);

    void deleteByFbId(String fbId);


}