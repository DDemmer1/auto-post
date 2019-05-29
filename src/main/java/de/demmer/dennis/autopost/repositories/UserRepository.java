package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Facebookuser, Integer> {

    int findIdByFbId(String fbId);
    Facebookuser findUserById(int Id);
    Facebookuser findUserByFbId(String id);


}
