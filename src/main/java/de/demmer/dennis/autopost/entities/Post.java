package de.demmer.dennis.autopost.entities;

import de.demmer.dennis.autopost.entities.user.User;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "posts")
@Log4j2
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

    @Column(nullable = false)
    private String time;

    @Column(columnDefinition = "boolean default 0")
    private boolean scheduled;

    @Column(columnDefinition = "boolean default 0")
    private boolean posted;

    @Column
    private String img;

    @Column
    private float longitude;

    @Column
    private float latitude;

    @Column(columnDefinition="boolean default true")
    private boolean enabled;

    public Post(String content, String date, Page page, String pageID) {
        this.content = content;
        this.pageID = pageID;
        this.date = date;
        this.page = page;
        this.user = page.getUser();
    }


    public int compareTo(Post post) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = dateFormat.parse(post.date + " " + post.time);
            date2 = dateFormat.parse(this.date + " " + this.time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1.compareTo(date2);
    }


}

