package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.Page;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface PageRepository extends CrudRepository<Page, Integer> {

    List<Page> findByUserId(int userID);

    Page findByIdAndUserId(int pageId, int userId);

    void deleteByUserId(int userId);


}