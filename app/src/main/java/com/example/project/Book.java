package com.example.project;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class Book {
    private String BookName;
    private String AuthorName;
    private String ID;
    private Boolean status;

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
