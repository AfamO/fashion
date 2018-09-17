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
}
