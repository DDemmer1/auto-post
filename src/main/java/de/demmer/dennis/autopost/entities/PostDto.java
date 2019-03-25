package de.demmer.dennis.autopost.entities;


import lombok.Data;


@Data
public class PostDto {

    private String content;
    private boolean enabled =true;
    private String date;
    private String time;
    private String img;
    private String latitude;
    private String longitude;
    private boolean posted =false;




}
