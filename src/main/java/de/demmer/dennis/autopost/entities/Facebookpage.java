package de.demmer.dennis.autopost.entities;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "facebookpage")
public class Facebookpage {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fbId;

    @ManyToOne
    @JoinColumn(name="facebookuser_id", nullable = false)
    private Facebookuser facebookuser;

    @OneToMany(mappedBy="facebookpage", cascade = CascadeType.ALL)
    private List<Facebookpost> facebookposts = new ArrayList<>();

    @Column(columnDefinition="boolean default true")
    private boolean enabled = true;

    @ElementCollection
    private Set<String> adminFbIds = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Facebookpage page = (Facebookpage) o;

        return fbId.equals(page.fbId);
    }


    @Override
    public String toString() {
        return name;
    }
}
