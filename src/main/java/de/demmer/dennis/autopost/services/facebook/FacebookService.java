package de.demmer.dennis.autopost.services.facebook;

import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.user.Facebookuser;

import java.util.List;

public interface FacebookService {

    String createFacebookAuthorizationURL();
    String createFacebookAccessToken(String code);
    String getName(String oAuthToken);
    String getEmail(String oAuthToken);
    String getID(String oAuthToken);
    void post(Facebookuser user, Facebookpost post);
    List<Facebookpage> getPages(String oAuthToken);
    String getProfilePicture(String oAuthToken);
    String getPageProfilePicture(String oAuthToken, String pageID);
}
