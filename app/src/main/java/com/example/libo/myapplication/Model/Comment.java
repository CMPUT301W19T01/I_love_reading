package com.example.libo.myapplication.Model;


import java.io.Serializable;

/**
 * The type Comment.
 */
public class Comment implements Serializable {
    private String BookId;
    private float rating;
    private String username;
    private String time;
    private String content;
    private Integer favor_number = 0;


    private String CommentId;


    /**
     * Gets user photo.
     *
     * @return the user photo
     */
    public String getUser_photo() {
        return user_photo;
    }

    /**
     * Sets user photo.
     *
     * @param user_photo the user photo
     */
    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    private String user_photo;

    /**
     * Instantiates a new Comment.
     */
    public Comment(){

    }

    /**
     * Instantiates a new Comment.
     *
     * @param rating   the rating
     * @param username the username
     * @param time     the time
     * @param content  the content
     */
    public Comment(float rating, String username, String time, String content){
        this.rating = rating;
        this.username = username;
        this.time = time;
        this.content = content;
    }

    /**
     * Gets rating.
     *
     * @return the rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * Sets rating.
     *
     * @param rating the rating
     */
    public void setRating(float rating) {
        this.rating = rating;
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
     * Gets time.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
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
     * Gets favor number.
     *
     * @return the favor number
     */
    public Integer getFavor_number() {
        return favor_number;
    }

    /**
     * Sets favor number.
     *
     * @param favor_number the favor number
     */
    public void setFavor_number(Integer favor_number) {
        this.favor_number = favor_number;
    }

    /**
     * Gets comment id.
     *
     * @return the comment id
     */
    public String getCommentId() {
        return CommentId;
    }

    /**
     * Sets comment id.
     *
     * @param commentId the comment id
     */
    public void setCommentId(String commentId) {
        CommentId = commentId;
    }


}
