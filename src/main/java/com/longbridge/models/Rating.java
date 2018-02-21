package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class Rating extends CommonFields {
    @OneToOne
    public User user;
    public int userCount;
    public int ratingCount;
    public double averageRating;
}
