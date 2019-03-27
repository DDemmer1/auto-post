package de.demmer.dennis.autopost.entities;

import de.demmer.dennis.autopost.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name="pages")
public class Page {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fbId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy="page", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @Column(columnDefinition="boolean default 1")
    private boolean enabled = true;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return fbId.equals(page.fbId);
    }

}
