package com.example.libo.myapplication.Model;

import java.io.Serializable;
import java.util.Date;

public class Request implements Serializable {
    private String sender;
    private String receiver;
    private String senderEmail;


    private String senderId;
    private String BookId;

    private boolean isBorrowed;

    private boolean isAccepted;
    private Date date;

    public Request(String sender, String receiver, String senderEmail,boolean isBorrowed,Date date) {
        this.sender = sender;
        this.receiver = receiver;
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
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
}
