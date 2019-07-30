package de.demmer.dennis.autopost.services.userhandling;


import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.repositories.FacebookuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacebookuserService {


    @Autowired
    FacebookpageRepository pageRepository;

    public boolean isAdminOfPage(String pageId, Facebookuser user){
        boolean isAdmin = false;
        List<Facebookpage> pageListByFbId = pageRepository.findAllByFbId(pageId);
        if(pageListByFbId!=null){
            for (Facebookpage facebookpage: pageListByFbId) {
                if(facebookpage.getFacebookuser().getId()== user.getId()){
                    isAdmin = true;
                }
            }
        }
        return isAdmin;
    }

}
