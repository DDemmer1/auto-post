package de.demmer.dennis.autopost.entities;

import de.demmer.dennis.autopost.entities.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "posts")
public class Post implements Comparable<Post> {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "page_id", nullable = false)
    protected Page page;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String pageID;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String date;

    @Column(columnDefinition = "boolean default 0")
    private boolean scheduled;

    @Column(columnDefinition = "boolean default 0")
    private boolean posted;

    private String img;

    private float longitude;

    private float latitude;

    public Post(String content, String date, Page page, String pageID) {
        this.content = content;
        this.pageID = pageID;
        this.date = date;
        this.page = page;
        this.user = page.getUser();
    }


    public int compareTo(Post post) {
        return this.date.compareTo(post.date);
    }


}

