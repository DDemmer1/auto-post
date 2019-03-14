package de.demmer.dennis.autopost.entities;


import lombok.Data;

@Data
public class PostDto {

    private String content;
    private boolean enabled =true;
    private String time;
    private String date;
    private String img;
    private String latitude;
    private String longitude;

}
