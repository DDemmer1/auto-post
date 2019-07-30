package de.demmer.dennis.autopost.services.facebook;

import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import org.springframework.stereotype.Service;

import java.util.List;


//TODO implement a direct connection to GraphAPI instead of using unsupported Spring Social


@Service
public class FacebookGraphApiService implements FacebookService {

    @Override
    public String createFacebookAuthorizationURL() {
        return null;
    }

    @Override
    public String createFacebookAccessToken(String code) {
        return null;
    }

    @Override
    public String getName(String oAuthToken) {
        return null;
    }

    @Override
    public String getEmail(String oAuthToken) {
        return null;
    }

    @Override
    public String getID(String oAuthToken) {
        return null;
    }

    @Override
    public void post(Facebookuser user, Facebookpost post) {

    }

    @Override
    public List<Facebookpage> getPages(String oAuthToken) {
        return null;
    }

    @Override
    public String getProfilePicture(String oAuthToken) {
        return null;
    }

    @Override
    public String getPageProfilePicture(String oAuthToken, String pageID) {
        return null;
    }
}
