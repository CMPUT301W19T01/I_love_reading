package com.example.libo.myapplication.Model;

public class FirebaseBook {
    private String bookname;
    private String uid;
    private String bookid;
    private Boolean status;
    private String AuthorName;
    private String Description;



    public FirebaseBook(){

    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
