package de.demmer.dennis.autopost.entities;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
public class User {


    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String fbId;

    @Column(nullable = false)
    private String oauthToken;

    @Column(nullable = false)
    private String oauthTokenSecret;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostGroup> groups;

    public User() {

    }

    public User(String fbId, String token, String secret) {
        this.fbId = fbId;
        this.oauthToken = token;
        this.oauthTokenSecret = secret;
    }

    public int getId() {
        return id;
    }

    public String getFbId() {
        return fbId;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public String getOauthTokenSecret() {
        return oauthTokenSecret;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fbId='" + fbId + '\'' +
                ", oauthToken='" + oauthToken + '\'' +
                ", oauthTokenSecret='" + oauthTokenSecret + '\''+
                '}';
    }
}

