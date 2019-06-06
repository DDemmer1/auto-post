package de.demmer.dennis.autopost.entities.user;

import de.demmer.dennis.autopost.entities.Facebookpage;
import de.demmer.dennis.autopost.entities.ImageFile;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "facebookuser")
public class Facebookuser {

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

    @OneToMany(mappedBy = "facebookuser", cascade = CascadeType.REMOVE)
    @ElementCollection
    private List<Facebookpage> pageList;


    @OneToMany(mappedBy = "facebookuser", cascade = CascadeType.REMOVE)
    @ElementCollection
    private List<ImageFile> imageFileList;



    public Facebookuser(String fbId, String oauthToken, String name, String email, List<Facebookpage> pageList) {
        this.fbId = fbId;
        this.oauthToken = oauthToken;
        this.name = name;
        this.email = email;
        this.pageList = pageList;
    }
}

