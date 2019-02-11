package de.demmer.dennis.autopost.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {


    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String fbId;

    @Column(nullable = false, columnDefinition="TEXT")
    private String oauthToken;

    @Column(nullable = false)
    private String oauthTokenSecret;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostGroup> groups;



    public User(String fbId, String token, String secret) {
        this.fbId = fbId;
        this.oauthToken = token;
        this.oauthTokenSecret = secret;
    }


}

