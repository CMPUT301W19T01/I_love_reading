package com.example.libo.myapplication.Model;

public class Request {
    private String sender;
    private String receiver;
    private String senderEmail;

    public Request(String sender, String receiver, String senderEmail) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderEmail = senderEmail;
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
}
