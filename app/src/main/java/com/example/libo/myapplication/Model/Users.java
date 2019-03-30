package com.example.libo.myapplication.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class Users {

    private String email;
    private String username;
    private String uid;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private String photo;

    private Integer ownbooknum;
    private Integer commentnum;
    private Integer borrowbooknum;

    public Users(){

    }
    public Users(String email, String username, String uid) {
        this.email = email;
        this.username = username;
        this.uid = uid;

    }

    public Users(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getOwnbooknum() {
        DatabaseReference storageRef = FirebaseDatabase.getInstance().getReference("bookcover").child(this.getUid());
        ownbooknum = 0;
        storageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Book book = ds.getValue(Book.class);
                    ownbooknum = ownbooknum +1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return ownbooknum;
    }

    public void setOwnbooknum(Integer ownbooknum) {
        this.ownbooknum = ownbooknum;
    }

    public Integer getCommentnum() {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("commentsTEST");
        commentnum =0;
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Comment comment = ds.getValue(Comment.class);
                    if (comment.getUsername().equals(getUsername())){
                        borrowbooknum = borrowbooknum +1;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return commentnum;
    }

    public void setCommentnum(Integer commentnum) {
        this.commentnum = commentnum;
    }

    public Integer getBorrowbooknum() {
        DatabaseReference borrowedRef = FirebaseDatabase.getInstance().getReference("borrowedBooks").child(this.getUid());
        borrowbooknum = 0;
        borrowedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Book book = ds.getValue(Book.class);
                        borrowbooknum = borrowbooknum +1;
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return borrowbooknum;
    }

    public void setBorrowbooknum(Integer borrowbooknum) {
        this.borrowbooknum = borrowbooknum;
    }
}

