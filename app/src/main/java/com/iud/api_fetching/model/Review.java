package com.iud.api_fetching.model;

import java.io.Serializable;

public class Review {

    private String rating;
    private String comment;
    private String date;
    private String reviewerName;

    public Review(String rating, String comment, String date, String reviewerName) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.reviewerName = reviewerName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

}
