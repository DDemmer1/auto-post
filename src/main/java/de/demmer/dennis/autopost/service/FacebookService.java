package de.demmer.dennis.autopost.service;


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

@Service
public class FacebookService {

    @Value("${spring.social.facebook.appId}")
    String facebookAppId;
    @Value("${spring.social.facebook.appSecret}")
    String facebookSecret;

    String accessToken ="";



    public String createFacebookAuthorizationURL(){
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri("http://localhost:8080/facebook");
        params.setScope("email,manage_pages,publish_pages");
        return oauthOperations.buildAuthorizeUrl(params);
    }


    public void createFacebookAccessToken(String code) {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, "http://localhost:8080/facebook", null);
        accessToken = accessGrant.getAccessToken();
    }



    public String getName() {
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = {"id", "name"};
        return facebook.fetchObject("me", String.class, fields);
    }

    public String setStatus(String status){
        Facebook facebook = new FacebookTemplate(accessToken);
        return facebook.feedOperations().updateStatus(status);
    }

    public String getEmail(){
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = {"id", "email"};
        return facebook.fetchObject("me", String.class, fields);

    }



    public int postOnPage(String post){

        try{
            Facebook facebook = new FacebookTemplate(accessToken);

            PageOperations pageOps = facebook.pageOperations();
            PagePostData ppd = new PagePostData("599851913798667");

            ppd.message(post);

            pageOps.post(ppd);

            return 1;
        }

        catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

}
