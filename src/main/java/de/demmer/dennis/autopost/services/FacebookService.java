package de.demmer.dennis.autopost.services;


import de.demmer.dennis.autopost.entities.Page;
import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.util.*;

@Transactional
@Log4j2
@Service
public class FacebookService {

    @Value("${spring.social.facebook.appId}")
    private String facebookAppId;
    @Value("${spring.social.facebook.appSecret}")
    private String facebookSecret;


    @Autowired
    PostRepository postRepository;


    public String createFacebookAuthorizationURL() {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri("http://localhost:8080/facebook");
        params.setScope("email," +
                "manage_pages," +
                "user_photos," +
                "publish_pages");

        return oauthOperations.buildAuthorizeUrl(params);
    }


    public String createFacebookAccessToken(String code) {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, "http://localhost:8080/facebook", null);
        return accessGrant.getAccessToken();
    }


    public String getName(String oAuthToken) {
        Facebook facebook = new FacebookTemplate(oAuthToken);
        String jsonName = facebook.fetchObject("me", String.class, "name");
        JSONObject jsonObject = new JSONObject(jsonName);
        return jsonObject.get("name").toString();
    }


    public String getEmail(String oAuthToken) {
        Facebook facebook = new FacebookTemplate(oAuthToken);
        String jsonEmail = facebook.fetchObject("me", String.class, "email");
        JSONObject jsonObject = new JSONObject(jsonEmail);
        return jsonObject.get("email").toString();

    }


    public String getID(String oAuthToken) {
        Facebook facebook = new FacebookTemplate(oAuthToken);
        String jsonID = facebook.fetchObject("me", String.class, "id");
        JSONObject jsonObject = new JSONObject(jsonID);
        return jsonObject.get("id").toString();
    }



    public void post(User user, Post post) {
        Facebook facebook = new FacebookTemplate(user.getOauthToken());



        PageOperations pageOps = facebook.pageOperations();
        try {
            pageOps.postPhoto(post.getPageID(),"613319732451885",new UrlResource("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/The_Scream.jpg/1024px-The_Scream.jpg"),post.getContent());
//            pageOps.postPhoto(post.getPageID(),"613319732451885",new UrlResource("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/The_Scream.jpg/1024px-The_Scream.jpg"),post.getContent());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        PagePostData ppd = new PagePostData(post.getPageID());
//        ppd.message(post.getContent());
//        ppd.toRequestParameters().add("picture","https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/The_Scream.jpg/1024px-The_Scream.jpg");
//        pageOps.post(ppd);


    }

    public Map<String,String> getPageIds(String oAuthToken) {

        Facebook facebook = new FacebookTemplate(oAuthToken);
        String accountData = facebook.fetchObject("me", String.class, "accounts");
        JSONObject jsonAcountData = new JSONObject(accountData).getJSONObject("accounts");
        JSONArray jsonArray = jsonAcountData.getJSONArray("data");

        SortedMap<String, String> pages = new TreeMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String id = obj.get("id").toString();
            String name = obj.get("name").toString();
            pages.put(id,name);
        }

        return pages;
    }



    public List<Page> getPages(String oAuthToken){

        List<Page> pageList = new ArrayList<>();

        Facebook facebook = new FacebookTemplate(oAuthToken);
        String accountData = facebook.fetchObject("me", String.class, "accounts");
        JSONObject jsonAcountData = new JSONObject(accountData).getJSONObject("accounts");
        JSONArray jsonArray = jsonAcountData.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String id = obj.get("id").toString();
            String name = obj.get("name").toString();
            Page page = new Page();
            page.setFbId(id);
            page.setName(name);
            pageList.add(page);
        }

        return pageList;

    }




    public String getProfilePicture(String oAuthToken) {
        Facebook facebook = new FacebookTemplate(oAuthToken);

        String accountData = facebook.fetchObject("me", String.class, "picture");

        JSONObject jsonAcountData = new JSONObject(accountData).getJSONObject("picture").getJSONObject("data");

        String url = jsonAcountData.get("url").toString();

        return url;

    }

    public String getPageProfilePicture(String oAuthToken, String pageID) {

        Facebook facebook = new FacebookTemplate(oAuthToken);
        String accountData = facebook.fetchObject("me", String.class, "accounts");
        JSONObject jsonAcountData = new JSONObject(accountData).getJSONObject("accounts");
        JSONArray jsonArray = jsonAcountData.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String id = obj.get("id").toString();

            if(id.equals(pageID)){
                return obj.toString();
            }

        }

        return "";
    }
}
