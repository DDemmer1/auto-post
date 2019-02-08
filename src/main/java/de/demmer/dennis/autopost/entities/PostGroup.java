package de.demmer.dennis.autopost.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name="groups")
public class PostGroup {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    /*TODO: CascadeType = ALL --> Rückgabe der PostID in savePost()*/
    @OneToMany(mappedBy="group", cascade = CascadeType.ALL)
    private List<Post> posts;

    @Column(columnDefinition="boolean default 0")
    private boolean enabled;

    @Column(nullable = true)
    private String description;


    public PostGroup(){

    }

    public PostGroup(String name, String description){
        this.name = name;
        this.description = description;
    }

    public PostGroup(String name, String description, User user){
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public void setUser(User user){
        this.user = user;
        for (Post post : posts) {
            post.setUser(user);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        Collections.sort(posts);
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        Collections.sort(this.posts);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void addPost(Post post){
        if(posts == null){
            posts = new ArrayList<Post>();
        }
        this.posts.add(post);
        Collections.sort(this.posts);
    }

}
