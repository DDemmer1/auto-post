package de.demmer.dennis.autopost.service;


import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.User;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PageOperations;
import org.springframework.social.facebook.api.PagePostData;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class FacebookService {

    @Value("${spring.social.facebook.appId}")
    private String facebookAppId;
    @Value("${spring.social.facebook.appSecret}")
    private String facebookSecret;



    public String createFacebookAuthorizationURL() {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri("http://localhost:8080/facebook");
        params.setScope("email," +
                "manage_pages," +
                "publish_pages,");

        return oauthOperations.buildAuthorizeUrl(params);
    }


    public String createFacebookAccessToken(String code) {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, "http://localhost:8080/facebook", null);
        return accessGrant.getAccessToken();
    }


    public String getName(User user) {
        Facebook facebook = new FacebookTemplate(user.getOauthToken());
        String[] fields = {"id", "name"};
        return facebook.fetchObject("me", String.class, fields);
    }


    public String getEmail(User user) {
        Facebook facebook = new FacebookTemplate(user.getOauthToken());
        String[] fields = {"email"};
        return facebook.fetchObject("me", String.class, fields);

    }


    public String getID(User user) {
        Facebook facebook = new FacebookTemplate(user.getOauthToken());
        String jsonID = facebook.fetchObject("me", String.class, "id");
        JSONObject jsonObject = new JSONObject(jsonID);
        return jsonObject.get("id").toString();
    }


    public void post(User user, Post post) {

        Facebook facebook = new FacebookTemplate(user.getOauthToken());
        PageOperations pageOps = facebook.pageOperations();
        PagePostData ppd = new PagePostData(post.getPageID());

        ppd.message(post.getContent());

        pageOps.post(ppd);


    }

}
