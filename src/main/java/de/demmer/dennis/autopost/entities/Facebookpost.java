package de.demmer.dennis.autopost.entities;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity(name = "facebookposts")
@Log4j2
public class Facebookpost implements Comparable<Facebookpost> {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "facebookpage", nullable = false)
    protected Facebookpage facebookpage;

    @ManyToOne
    @JoinColumn(name = "facebookuser_id", nullable = false)
    private Facebookuser facebookuser;

    @Column(name ="facebookpage_id")
    private String facebookpageID;

    @Column (length= 10485760, nullable = false)
    private String content;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String time;

    @Column(columnDefinition = "boolean default false")
    private boolean scheduled;

    @Column(columnDefinition = "boolean default false")
    private boolean posted;

    @Column(columnDefinition = "boolean default false")
    private boolean error;

    @Column (length= 10485760)
    private String img;

    @Column
    private float longitude;

    @Column
    private float latitude;

    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

    public Facebookpost(String content, String date, Facebookpage page, String facebookpageID) {
        this.content = content;
        this.facebookpageID = facebookpageID;
        this.date = date;
        this.facebookpage = page;
        this.facebookuser = page.getFbuser();
    }


    public int compareTo(Facebookpost post) {

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
                ", facebookpage=" + facebookpage.getFbId() +
                ", fbuser=" + facebookuser.getFbId() +
                ", facebookpageID='" + facebookpageID + '\'' +
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

