package com.example.libo.myapplication.Model;

import android.graphics.Bitmap;

import com.example.libo.myapplication.BookStatus;

/**
 * The type Book.
 */
public class Book {
    private String OwnerId;
    private String BookName;
    private String AuthorName;
    private String ID;
    private Boolean status;
    private String Description;
    private String Classification;

    private Boolean isOwnerConfirmed;

    private Boolean isBorrowerConfirmed;

    private BookStatus new_status;

    private String bookcoverUri;
    private Float rating;
    private Bitmap BookCover;

    /**
     * Gets owner name.
     *
     * @return the owner name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Sets owner name.
     *
     * @param ownerName the owner name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    private String ownerName;

    /**
     * Instantiates a new Book.
     */
    public Book(){
        isBorrowerConfirmed = false;
        isOwnerConfirmed = false;
    }

    /**
     * Instantiates a new Book.
     *
     * @param BookName       the book name
     * @param AuthorName     the author name
     * @param ID             the id
     * @param status         the status
     * @param Description    the description
     * @param Classification the classification
     * @param ownerId        the owner id
     */
    public Book(String BookName, String AuthorName, String ID, Boolean status, String Description, String Classification,String ownerId){
        this.setBookName(BookName);
        this.setStatus(status);
        this.setID(ID);
        this.setAuthorName(AuthorName);
        this.setDescription(Description);
        this.setClassification(Classification);
        this.OwnerId = ownerId;
        this.new_status = BookStatus.available;
    }

    /**
     * Gets bookcover uri.
     *
     * @return the bookcover uri
     */
    public String getBookcoverUri() {
        return bookcoverUri;
    }

    /**
     * Sets bookcover uri.
     *
     * @param bookcoverUri the bookcover uri
     */
    public void setBookcoverUri(String bookcoverUri) {
        this.bookcoverUri = bookcoverUri;
    }

    /**
     * Gets owner confirmed.
     *
     * @return the owner confirmed
     */
    public Boolean getOwnerConfirmed() {
        return isOwnerConfirmed;
    }

    /**
     * Sets owner confirmed.
     *
     * @param ownerConfirmed the owner confirmed
     */
    public void setOwnerConfirmed(Boolean ownerConfirmed) {
        isOwnerConfirmed = ownerConfirmed;
    }

    /**
     * Gets borrower confirmed.
     *
     * @return the borrower confirmed
     */
    public Boolean getBorrowerConfirmed() {
        return isBorrowerConfirmed;
    }

    /**
     * Sets borrower confirmed.
     *
     * @param borrowerConfirmed the borrower confirmed
     */
    public void setBorrowerConfirmed(Boolean borrowerConfirmed) {
        isBorrowerConfirmed = borrowerConfirmed;
    }

    /**
     * Gets new status.
     *
     * @return the new status
     */
    public BookStatus getNew_status() {
        return new_status;
    }

    /**
     * Sets new status.
     *
     * @param new_status the new status
     */
    public void setNew_status(BookStatus new_status) {
        this.new_status = new_status;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        Description = description;
    }


    /**
     * Gets book name.
     *
     * @return the book name
     */
    public String getBookName() {
        return BookName;
    }

    /**
     * Sets book name.
     *
     * @param bookName the book name
     */
    public void setBookName(String bookName) {
        BookName = bookName;
    }

    /**
     * Gets author name.
     *
     * @return the author name
     */
    public String getAuthorName() {
        return AuthorName;
    }

    /**
     * Sets author name.
     *
     * @param authorName the author name
     */
    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getID() {
        return ID;
    }

    /**
     * Sets id.
     *
     * @param ID the id
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Boolean getStatus() {
        return status; // True means the item is borrowed, False means the item is still available
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Boolean status) {
        this.status = status; // True means the item is borrowed, False means the item is still available
    }

    /**
     * Gets classification.
     *
     * @return the classification
     */
    public String getClassification() {
        return Classification;
    }

    /**
     * Sets classification.
     *
     * @param classification the classification
     */
    public void setClassification(String classification) {
        this.Classification = classification;
    }

    /**
     * Gets rating.
     *
     * @return the rating
     */
    public Float getRating() {
        return rating;
    }

    /**
     * Sets rating.
     *
     * @param rating the rating
     */
    public void setRating(Float rating) {
        this.rating = rating;
    }

    /**
     * Gets book cover.
     *
     * @return the book cover
     */
    public Bitmap getBookCover() {
        return BookCover;
    }

    /**
     * Sets book cover.
     *
     * @param bookCover the book cover
     */
    public void setBookCover(Bitmap bookCover) {
        BookCover = bookCover;
    }

    /**
     * Gets owner id.
     *
     * @return the owner id
     */
    public String getOwnerId() {
        return OwnerId;
    }

    /**
     * Sets owner id.
     *
     * @param ownerId the owner id
     */
    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

}