package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.PostGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface GroupRepository extends CrudRepository<PostGroup, Integer> {

    List<PostGroup> findByUserId(int userID);

    PostGroup findByIdAndUserId(int groupId, int userId);

    boolean findEnabledByUserIdAndId(int userId, int groupId);

    void deleteByUserId(int userId);


}