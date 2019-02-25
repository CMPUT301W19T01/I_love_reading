package com.example.project;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class Book {
    private String BookName;
    private String AuthorName;
    private String ID;
    private Boolean status;
    private String Description;

    public Book(String BookName, String AuthorName, String ID, Boolean status, String Description){
        this.setBookName(BookName);
        this.setStatus(status);
        this.setID(ID);
        this.setAuthorName(AuthorName);
        this.setDescription(Description);
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
}
