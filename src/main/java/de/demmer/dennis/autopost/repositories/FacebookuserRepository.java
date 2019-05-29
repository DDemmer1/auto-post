package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import org.springframework.data.repository.CrudRepository;

public interface FacebookuserRepository extends CrudRepository<Facebookuser, Integer> {

    int findIdByFbId(String fbId);
    Facebookuser findFacebookuserById(int Id);
    Facebookuser findFacebookuserByFbId(String id);


}
