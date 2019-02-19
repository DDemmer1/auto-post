package de.demmer.dennis.autopost.entities.user;

import de.demmer.dennis.autopost.entities.PostGroup;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;


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

    @Column
    private String name;

    @Column
    private String email;

    @ElementCollection
    private Map<String,String> pages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @ElementCollection
    private List<PostGroup> groups;


    public User(String fbId, String oauthToken, String name, String email, Map<String,String> pages) {
        this.fbId = fbId;
        this.oauthToken = oauthToken;
        this.name = name;
        this.email = email;
        this.pages = pages;
    }


}

