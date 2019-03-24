package com.example.libo.myapplication.Model;

import java.io.Serializable;
import java.util.Date;

public class Request implements Serializable {

    private String sender;
    private String receiverId;
    private String senderEmail;
    private String senderId;
    private String BookId;

    //isBorrowed is used to check sender wants to borrow or return
    private boolean isBorrowed;
    //isAccept is used to hide the button when users select accept button
    private boolean isAccepted = false;
    private Date date;
    private LatLng latLng = null;

    public Request(String sender, String receiver, String senderEmail, boolean isBorrowed, Date date) {
        this.sender = sender;
        this.receiverId = receiver;
        this.senderEmail = senderEmail;
        this.isBorrowed = isBorrowed;
        this.date = date;
    }

    public Request(){

    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getBookId() {
        return BookId;
    }

    public void setBookId(String bookId) {
        BookId = bookId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiverId;
    }

    public void setReceiver(String receiver) {
        this.receiverId = receiver;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
