package com.example.project;

public class Comment {
    private Float rating;
    private String username;
    private String time;
    private String content;

    public Comment(Float rating, String username, String time, String content){
        this.rating = rating;
        this.username = username;
        this.time = time;
        this.content = content;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
