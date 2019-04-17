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

    @Column (length= 10485760)
    private String img;

    @Column
    private float longitude;

    @Column
    private float latitude;

    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

    public Post(String content, String date, Page page, String pageID) {
        this.content = content;
        this.pageID = pageID;
        this.date = date;
        this.page = page;
        this.user = page.getUser();
    }


    public int compareTo(Post post) {

        if (this.posted) {
            return 1;
        } else if (post.isPosted()) {
            return -1;
        }

        StringBuilder time1 = new StringBuilder(post.time.replace(":", ""));
        StringBuilder time2 = new StringBuilder(this.time.replace(":", ""));

        while (time1.length() < 4) {
            time1.insert(0, "0");
        }

        while (time2.length() < 4) {
            time2.insert(0, "0");
        }

        String dateTime1 = post.date.replace("-", "") + time1.toString();
        String dateTime2 = this.date.replace("-", "") + time2.toString();

        return dateTime2.compareTo(dateTime1);

    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", page=" + page.getFbId() +
                ", user=" + user.getFbId() +
                ", pageID='" + pageID + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", scheduled=" + scheduled +
                ", posted=" + posted +
                ", img='" + img + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", enabled=" + enabled +
                '}';
    }
}

