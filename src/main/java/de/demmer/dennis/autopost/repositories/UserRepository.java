package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    int findIdByFbId(String fbId);
    User findUserById(int Id);


}
