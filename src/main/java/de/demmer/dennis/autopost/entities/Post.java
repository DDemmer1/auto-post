package de.demmer.dennis.autopost.entities;

import javax.persistence.*;

@Entity(name="posts")
public class Post implements Comparable<Post>
{

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    protected PostGroup group;

    @ManyToOne
    @JoinColumn (name="user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String date;

    @Column(columnDefinition="boolean default 0")
    private boolean scheduled;

    @Column(columnDefinition="boolean default 0")
    private boolean posted;

    private String img;

    private float longitude;

    private float latitude;


    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Post(){

    }

    public Post (String content, PostGroup group){
        this.content = content;
        this.group = group;
        this.user = group.getUser();
    }

    public Post(String content, String date, PostGroup group){
        this.content = content;
        this.date  = date;
        this.group = group;
        this.user = group.getUser();
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public int getId() {
        return id;
    }

    public PostGroup getGroup() {
        return group;
    }

    public User getUser() {
        return user;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int compareTo(Post post) {
        return this.date.compareTo(post.date);
    }


}

