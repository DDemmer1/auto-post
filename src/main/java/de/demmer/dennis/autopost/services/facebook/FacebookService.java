package de.demmer.dennis.autopost.services.facebook;


import com.restfb.*;
import com.restfb.exception.FacebookException;
import com.restfb.types.FacebookType;
import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.ImageFile;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.repositories.ImageRepository;
import de.demmer.dennis.autopost.services.scheduling.ScheduleService;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.util.MultiMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Handels everything Facebook related. Can retrieve and send data from/to Facebook.
 */
@Transactional(rollbackFor = UserException.class)
@Log4j2
@Service
public class FacebookService {

    @Value("${spring.social.facebook.appId}")
    private String facebookAppId;
    @Value("${spring.social.facebook.appSecret}")
    private String facebookSecret;
    @Value("${facebook.redirect.url}")
    private String redirectURL;


    @Autowired
    FacebookpostRepository postRepository;

    @Autowired
    ApplicationContext ctx;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ImageRepository imageRepository;


    /**
     * Creates a URL which can be used to login a user to the web app.
     * The link will redirect you to the specified redirect value, defined in the application.properties value.
     *
     * @return The URL used to login
     */
    public String createFacebookAuthorizationURL() {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirectURL);
        params.setScope("email," +
                "manage_pages," +
                "pages_show_list," +
                "publish_pages");

        return oauthOperations.buildAuthorizeUrl(params);
    }


    /**
     * Creates the oAuth token from the return code of the facebook login link
     *
     * @param code
     * @return The oauth token used for verification in every
     */
    public String createFacebookAccessToken(String code) {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, redirectURL, null);
        return accessGrant.getAccessToken();
    }


    /**
     * Returns the name of the user with the respective oAuthToken
     *
     * @param oAuthToken
     * @return The name of the user
     */
    public String getName(String oAuthToken) {
        Facebook facebook = new FacebookTemplate(oAuthToken);
        String jsonName = facebook.fetchObject("me", String.class, "name");
        JSONObject jsonObject = new JSONObject(jsonName);
        return jsonObject.get("name").toString();
    }

    /**
     * Returns the email of the user with the respective oAuthToken
     *
     * @param oAuthToken
     * @return The email of the user
     */
    public String getEmail(String oAuthToken) {
        Facebook facebook = new FacebookTemplate(oAuthToken);
        String jsonEmail = facebook.fetchObject("me", String.class, "email");
        JSONObject jsonObject = new JSONObject(jsonEmail);
        return jsonObject.get("email").toString();

    }

    /**
     * Returns the Facebook id of the user with the respective oAuthToken
     *
     * @param oAuthToken
     * @return The Facebook id of the user
     */
    public String getID(String oAuthToken) {
        Facebook facebook = new FacebookTemplate(oAuthToken);
        String jsonID = facebook.fetchObject("me", String.class, "id");
        JSONObject jsonObject = new JSONObject(jsonID);
        return jsonObject.get("id").toString();
    }


    public void post(Facebookuser user, Facebookpost post) {
        try {
            boolean isMultiImagePost = post.getImageFile() != null && post.getImageFile().size() > 0;
            boolean isImageUrlPost = post.getImg() != null && !post.getImg().equals("");
            boolean isMessageContentPost = post.getContent() != null && !post.getContent().equals("");
            if (isMultiImagePost) {
                log.info("MultiImagePost; ImageFiles: " + post.getImageFile().size());
                log.info("MultiImagePost; ImageUrl: " + post.getImg());
                postMultiplePictures(user, post);
                return;
            } else if (isImageUrlPost) {
                log.info("SimpleImageURLPost; ImageUrl: " + post.getImg());
                Facebook facebook = new FacebookTemplate(user.getOauthToken());
                String pageID = post.getPageID();
                String pageAccessToken = facebook.pageOperations().getAccessToken(pageID);
                FacebookClient fbClient = new DefaultFacebookClient(pageAccessToken, Version.VERSION_3_3);
                BinaryAttachment pictureAttachment = BinaryAttachment.with(post.getImg(), toByteArray(post.getImg()));
                fbClient.publish(pageID + "/photos", FacebookType.class, pictureAttachment, Parameter.with("message", post.getContent()));
                return;
            } else if (isMessageContentPost) {
                log.info("SimpleMessagePost: " + post.getContent());
                Facebook facebook = new FacebookTemplate(user.getOauthToken());
                String pageID = post.getPageID();
                String pageAccessToken = facebook.pageOperations().getAccessToken(pageID);
                FacebookClient fbClient = new DefaultFacebookClient(pageAccessToken, Version.VERSION_3_3);
                fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * Posts a @{@link Facebookpost} on Facebook
     *
     * @param user The user who posts the @{@link Facebookpost}
     * @param post The @{@link Facebookpost} which is about to be send
     */
    public void postOld(Facebookuser user, Facebookpost post) {
        if (post.getImageFile().size() > 1) {
            log.info("Size is: " + post.getImageFile().size());
            postMultiplePictures(user, post);
            return;
        }
        //TODO save in memory not on disk
        scheduleService.cancelScheduling(post);

        //Image URL in facebook post. Temporary downloads the image to prevent silent Facebook errors
        int random = new Random().nextInt(9999);
        if (!post.getImg().equals("")) {
            try {
                URL url = new URL(post.getImg());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

                InputStream is = con.getInputStream();
                OutputStream os = new FileOutputStream(random + ".png");

                byte[] b = new byte[2048];
                int length;

                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }

                is.close();
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //deletes tmp after one minute from server
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String picture = random + ".png";
                    try {
                        TimeUnit.MINUTES.sleep(1);
                        try {
                            Files.delete(Paths.get(picture));
                            ctx.getResource("file:" + picture).getFile().delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


        //Temporary download image file to prevent facebook errors
        if (post.getImageFile().get(0) != null) {
            try {
                byte[] data = post.getImageFile().get(0).getData();
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                BufferedImage bImage2 = ImageIO.read(bis);
                ImageIO.write(bImage2, "png", new File(random + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }


            //deletes tmp after one minute from server
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String picture = random + ".png";
                    try {
                        TimeUnit.MINUTES.sleep(1);
                        try {
                            Files.delete(Paths.get(picture));
                            ctx.getResource("file:" + picture).getFile().delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


        Facebook facebook = new FacebookTemplate(user.getOauthToken());
        PageOperations pageOps = facebook.pageOperations();
        try {
            if ((!post.getImg().equals("") && post.getImg() != null) || post.getImageFile() != null) { //post with image URL
                pageOps.postPhoto(post.getPageID(), post.getPageID(), ctx.getResource("file:" + random + ".png"), post.getContent());
            } else if (post.getImageFile() == null) { //just text to post
                log.info(facebook.pageOperations().getAccessToken(post.getPageID()));
                PagePostData ppd = new PagePostData(post.getPageID());
                ppd.message(post.getContent());
                pageOps.post(ppd);
            }
        } catch (InsufficientPermissionException ip) {
            log.error("Missing permission: " + ip.getRequiredPermission());
            ip.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("post successful: [fbuser: '" + user.getName() + "', facebookpage: '" + post.getFacebookpage().getName() + "', content: '" + post.getContent() + "']");

    }


    /**
     * Returns all permissioned pages of the user with the oAuth token  @{@link Facebookpage}
     *
     * @param oAuthToken
     * @return A List of @{@link Facebookpage}
     */
    public List<Facebookpage> getPages(String oAuthToken) {

        List<Facebookpage> pageList = new ArrayList<>();

        Facebook facebook = new FacebookTemplate(oAuthToken);
        try {


            String accountData = facebook.fetchObject("me", String.class, "accounts");
            JSONObject jsonAcountData = new JSONObject(accountData).getJSONObject("accounts");
            JSONArray jsonArray = jsonAcountData.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id = obj.get("id").toString();
                String name = obj.get("name").toString();
                Facebookpage page = new Facebookpage();
                page.setFbId(id);
                page.setName(name);
                pageList.add(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pageList;

    }


    /**
     * Gives you the URL of the profile picture of the user
     *
     * @param oAuthToken The oAuth token of the user
     * @return The profile picture URL
     */
    public String getProfilePicture(String oAuthToken) {
        Facebook facebook = new FacebookTemplate(oAuthToken);

        String accountData = facebook.fetchObject("me", String.class, "picture");

        JSONObject jsonAcountData = new JSONObject(accountData).getJSONObject("picture").getJSONObject("data");

        String url = jsonAcountData.get("url").toString();

        return url;

    }

    /**
     * Returns the image url of the facebook page
     *
     * @param oAuthToken
     * @param pageID
     * @return image url of the facebook page
     */
    public String getPageProfilePicture(String oAuthToken, String pageID) {

        Facebook facebook = new FacebookTemplate(oAuthToken);
        String accountData = facebook.fetchObject("me", String.class, "accounts");
        JSONObject jsonAcountData = new JSONObject(accountData).getJSONObject("accounts");
        JSONArray jsonArray = jsonAcountData.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String id = obj.get("id").toString();

            if (id.equals(pageID)) {
                return obj.toString();
            }
        }

        return "";
    }


    public void postMultiplePictures(Facebookuser user, Facebookpost post) {
        Facebook facebook = new FacebookTemplate(user.getOauthToken());
        String pageID = post.getPageID();
        String pageAccessToken = facebook.pageOperations().getAccessToken(pageID);
        FacebookClient fbClient = new DefaultFacebookClient(pageAccessToken, Version.VERSION_3_3);
        List<ImageFile> images = imageRepository.findAllByFacebookpost_Id(post.getId());
        log.info("Images connected with post: " + images.size());

        List<Parameter> parameters = new ArrayList<>();
        int index = 0;
        for (ImageFile file : images) {
            String id = fbClient.publish(pageID + "/photos", FacebookType.class, BinaryAttachment.with(file.getFileName(), file.getData()), Parameter.with("published", "false")).getId();
            log.info("Id of " + index + ": " + id);
            parameters.add(Parameter.with("attached_media[" + index + "]", "{\"media_fbid\":\"" + id + "\"}"));
            index++;
        }
        //Image URL exists
        if (post.getImg() != null && !post.getImg().equals("")) {
            String id = fbClient.publish(pageID + "/photos", FacebookType.class, BinaryAttachment.with("image", toByteArray(post.getImg())), Parameter.with("published", "false")).getId();
            log.info("Id of " + index + ": " + id);
            parameters.add(Parameter.with("attached_media[" + index + "]", "{\"media_fbid\":\"" + id + "\"}"));
        }
        log.info("Parameters created: " + parameters.size());

        parameters.forEach(params -> System.out.println(params.toString()));

        if (!parameters.isEmpty()) {
            //workaround because restFb does not handle the varargs argument right
            switch (parameters.size()) {
                case 1:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0));
                    break;
                case 2:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1));
                    break;
                case 3:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1), parameters.get(2));
                    break;
                case 4:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3));
                    break;
                case 5:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4));
                    break;
                case 6:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4), parameters.get(5));
                    break;
                case 7:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4), parameters.get(5), parameters.get(6));
                    break;
                case 8:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4), parameters.get(5), parameters.get(6), parameters.get(7));
                    break;
                case 9:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4), parameters.get(5), parameters.get(6), parameters.get(7), parameters.get(8));
                    break;
                case 10:
                    fbClient.publish(pageID + "/feed", FacebookType.class, Parameter.with("message", post.getContent()), parameters.get(0), parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4), parameters.get(5), parameters.get(6), parameters.get(7), parameters.get(8), parameters.get(9));
                    break;
                default:
                    break;
            }
        } else {
            try {
                throw new Exception();
            } catch (Exception e) {
                log.error("Parameter list was empty!");
                e.printStackTrace();
            }
        }
    }


    private byte[] toByteArray(String postImageUrl) {
        byte[] byteArray = null;
        try {
            URL url = new URL(postImageUrl);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            try (InputStream inputStream = url.openStream()) {
                int n = 0;
                byte[] buffer = new byte[1024];
                while (-1 != (n = inputStream.read(buffer))) {
                    output.write(buffer, 0, n);
                }
            }
            byteArray = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (byteArray == null) {
            throw new NullPointerException("Byte Array is null");
        }
        return byteArray;
    }

}
