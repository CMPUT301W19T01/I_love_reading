package com.example.libo.myapplication.Model;


import java.io.Serializable;

public class Comment implements Serializable {
    private String BookId;
    private double rating;
    private String username;
    private String time;
    private String content;

    public Comment(){

    }

    public Comment(double rating, String username, String time, String content){
        this.rating = rating;
        this.username = username;
        this.time = time;
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
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

    public String getBookId() {
        return BookId;
    }

    public void setBookId(String bookId) {
        BookId = bookId;
    }


}
