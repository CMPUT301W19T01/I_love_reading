package com.example.libo.myapplication.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Book {
    private String OwnerId;
    private String BookName;
    private String AuthorName;
    private String ID;
    private Boolean status;
    private String Description;
    private ArrayList<String> Classification;
    private ArrayList<Comment> Comments;

    private Float rating;
    private Bitmap BookCover;

    public Book(){
        this.Comments = new ArrayList<Comment>();
    }

    public Book(String BookName, String AuthorName, String ID, Boolean status, String Description, ArrayList<String> Classification,String ownerId){
        this.setBookName(BookName);
        this.setStatus(status);
        this.setID(ID);
        this.setAuthorName(AuthorName);
        this.setDescription(Description);
        this.setClassification(Classification);
        this.Comments = new ArrayList<Comment>();
        this.OwnerId = ownerId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Boolean getStatus() {
        return status; // True means the item is borrowed, False means the item is still available
    }

    public void setStatus(Boolean status) {
        this.status = status; // True means the item is borrowed, False means the item is still available
    }
    public ArrayList<String> getClassification() {
        return Classification;
    }

    public void setClassification(ArrayList<String> classification) {
        this.Classification = classification;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Bitmap getBookCover() {
        return BookCover;
    }

    public void setBookCover(Bitmap bookCover) {
        BookCover = bookCover;
    }

    public ArrayList<Comment> getComments() {
        return Comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        Comments = comments;
    }

    public void addComments(Comment newComment){
        this.Comments.add(newComment);
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

}