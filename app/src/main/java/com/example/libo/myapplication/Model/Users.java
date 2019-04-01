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

/**
 * The type Users.
 */
public class Users {

    private String email;
    private String username;
    private String uid;

    /**
     * Gets photo.
     *
     * @return the photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets photo.
     *
     * @param photo the photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private String photo;

    private int ownbooknum;
    private int commentnum;
    private int borrowbooknum = 0;

    /**
     * Instantiates a new Users.
     */
    public Users(){

    }

    /**
     * Instantiates a new Users.
     *
     * @param email    the email
     * @param username the username
     * @param uid      the uid
     */
    public Users(String email, String username, String uid) {
        this.email = email;
        this.username = username;
        this.uid = uid;

    }

    /**
     * Instantiates a new Users.
     *
     * @param email the email
     * @param uid   the uid
     */
    public Users(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets ownbooknum.
     *
     * @return the ownbooknum
     */
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

    /**
     * Sets ownbooknum.
     *
     * @param ownbooknum the ownbooknum
     */
    public void setOwnbooknum(int ownbooknum) {
        this.ownbooknum = ownbooknum;
    }

    /**
     * Gets commentnum.
     *
     * @return the commentnum
     */
    public int getCommentnum() {
        return this.commentnum;
    }

    /**
     * Sets commentnum.
     *
     * @param commentnum the commentnum
     */
    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    /**
     * Gets borrowbooknum.
     *
     * @return the borrowbooknum
     */
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

    /**
     * Sets borrowbooknum.
     *
     * @param borrowbooknum the borrowbooknum
     */
    public void setBorrowbooknum(int borrowbooknum) {
        this.borrowbooknum = borrowbooknum;
    }
}

