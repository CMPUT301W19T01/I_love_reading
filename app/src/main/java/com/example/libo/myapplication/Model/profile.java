package com.example.libo.myapplication.Model;

import android.graphics.Bitmap;

public class profile{
    private String UserName;
    private String UserEmail;
    private String Location;
    private Bitmap UserPic;

    public profile(String userName, String userEmail, String location, Bitmap userPic) {
        UserName = userName;
        UserEmail = userEmail;
        Location = location;
        UserPic = userPic;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Bitmap getUserPic() {
        return UserPic;
    }

    public void setUserPic(Bitmap userPic) {
        UserPic = userPic;
    }
}