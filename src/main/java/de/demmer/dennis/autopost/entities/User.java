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

    @Column
    private String name;

    @Column
    private String email;

    @ElementCollection
    private List<String> pageIds;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostGroup> groups;

    //TODO delete
    public User(String fbId, String token) {
        this.fbId = fbId;
        this.oauthToken = token;
    }


    protected User(String fbId, String oauthToken, String name, String email, List<String> pageIds) {
        this.fbId = fbId;
        this.oauthToken = oauthToken;
        this.name = name;
        this.email = email;
        this.pageIds = pageIds;
    }
}

