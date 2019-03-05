package de.demmer.dennis.autopost.entities.user;

import de.demmer.dennis.autopost.entities.Page;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @ElementCollection
    private List<Page> pageList;


    public User(String fbId, String oauthToken, String name, String email, List<Page> pageList) {
        this.fbId = fbId;
        this.oauthToken = oauthToken;
        this.name = name;
        this.email = email;
        this.pageList = pageList;
    }
}

