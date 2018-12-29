package com.example.mateusz.chatapplication.Model;

/**
 * Created by Mateusz on 29.12.2018.
 */

public class User {
    private String id;
    private String username;
    private String imageURL;

    public User(String id, String username, String imageURL){
        this.id = id;
        this.imageURL = imageURL;
        this.username = username;
    }

    public User(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
