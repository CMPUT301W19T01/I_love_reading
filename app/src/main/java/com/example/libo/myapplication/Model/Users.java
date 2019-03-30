package com.example.libo.myapplication.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import static de.greenrobot.event.EventBus.TAG;

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

    private int ownbooknum;
    private int commentnum;
    private int borrowbooknum = 0;

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

    public int getOwnbooknum() {
        DatabaseReference storageRef = FirebaseDatabase.getInstance().getReference("books").child(this.getUid());
        storageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ownbooknum = (int) dataSnapshot.getChildrenCount();
                setOwnbooknum(ownbooknum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d(TAG ,"Book num "+ this.ownbooknum);
        return this.ownbooknum;
    }

    public void setOwnbooknum(int ownbooknum) {
        this.ownbooknum = ownbooknum;
    }

    public int getCommentnum() {
        return this.commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    public int getBorrowbooknum() {
        DatabaseReference borrowedRef = FirebaseDatabase.getInstance().getReference("acceptedBook").child(this.getUid());
        borrowbooknum = 0;
        borrowedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                borrowbooknum = (int) dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return borrowbooknum;
    }

    public void setBorrowbooknum(int borrowbooknum) {
        this.borrowbooknum = borrowbooknum;
    }
}

