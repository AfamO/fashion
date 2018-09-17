package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Rating extends CommonFields {
    @OneToOne
    private User user;
    private int userCount;
    private int ratingCount;
    private double averageRating;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Rating() {
    }

    public Rating(User user, int userCount, int ratingCount, double averageRating) {

        this.user = user;
        this.userCount = userCount;
        this.ratingCount = ratingCount;
        this.averageRating = averageRating;
    }
}
