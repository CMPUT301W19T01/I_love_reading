package com.example.libo.myapplication.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Request.
 */
public class Request implements Serializable {

    private String sender;
    private String receiverId;
    private String senderEmail;
    private String senderId;
    private String BookId;
    private String bookName;
    private String requestId;

    //isBorrowed is true means the user want to borrow the book; false means the user wants to return the book
    private boolean isBorrowed = true;
    //isAccept is used to hide the button when users select accept button
    private boolean isAccepted = false;
    private Date date;
    private LatLng latLng = null;
    private boolean notification_own = true;
    private boolean notification_borrow = true;



    /**
     * Instantiates a new Request.
     *
     * @param sender      the sender
     * @param receiver    the receiver
     * @param senderEmail the sender email
     * @param date        the date
     */
    public Request(String sender, String receiver, String senderEmail, Date date) {
        this.sender = sender;
        this.receiverId = receiver;
        this.senderEmail = senderEmail;
        this.date = date;
    }

    /**
     * Instantiates a new Request.
     */
    public Request(){

    }


    /**
     * Gets sender id.
     *
     * @return the sender id
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Sets sender id.
     *
     * @param senderId the sender id
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * Gets book id.
     *
     * @return the book id
     */
    public String getBookId() {
        return BookId;
    }

    /**
     * Sets book id.
     *
     * @param bookId the book id
     */
    public void setBookId(String bookId) {
        BookId = bookId;
    }

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets sender.
     *
     * @param sender the sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Gets receiver.
     *
     * @return the receiver
     */
    public String getReceiver() {
        return receiverId;
    }

    /**
     * Sets receiver.
     *
     * @param receiver the receiver
     */
    public void setReceiver(String receiver) {
        this.receiverId = receiver;
    }

    /**
     * Gets sender email.
     *
     * @return the sender email
     */
    public String getSenderEmail() {
        return senderEmail;
    }

    /**
     * Sets sender email.
     *
     * @param senderEmail the sender email
     */
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Is borrowed boolean.
     *
     * @return the boolean
     */
    public boolean isBorrowed() {
        return isBorrowed;
    }

    /**
     * Sets borrowed.
     *
     * @param borrowed the borrowed
     */
    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    /**
     * Is accepted boolean.
     *
     * @return the boolean
     */
    public boolean isAccepted() {
        return isAccepted;
    }

    /**
     * Sets accepted.
     *
     * @param accepted the accepted
     */
    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    /**
     * Gets lat lng.
     *
     * @return the lat lng
     */
    public LatLng getLatLng() {
        return latLng;
    }

    /**
     * Sets lat lng.
     *
     * @param latLng the lat lng
     */
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    /**
     * Gets book name.
     *
     * @return the book name
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * Sets book name.
     *
     * @param bookName the book name
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * Gets request id.
     *
     * @return the request id
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets request id.
     *
     * @param requestId the request id
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isNotification_own() {
        return notification_own;
    }

    public void setNotification_own(boolean notification_own) {
        this.notification_own = notification_own;
    }

    public boolean isNotification_borrow() {
        return notification_borrow;
    }

    public void setNotification_borrow(boolean notification_borrow) {
        this.notification_borrow = notification_borrow;
    }
}
