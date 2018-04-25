package com.longbridge.models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

/**
 * Created by Longbridge on 24/04/2018.
 */
@Entity
public class ProductRating extends CommonFields{
    @OneToOne
    private User user;
    private int deliveryTimeRating;
    private int productQualityRating;
    private int serviceRating;
    private String subject;

    @Lob
    private String review;


    public ProductRating() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDeliveryTimeRating() {
        return deliveryTimeRating;
    }

    public void setDeliveryTimeRating(int deliveryTimeRating) {
        this.deliveryTimeRating = deliveryTimeRating;
    }

    public int getProductQualityRating() {
        return productQualityRating;
    }

    public void setProductQualityRating(int productQualityRating) {
        this.productQualityRating = productQualityRating;
    }

    public int getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(int serviceRating) {
        this.serviceRating = serviceRating;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public ProductRating(User user, int deliveryTimeRating, int productQualityRating, int serviceRating, String subject, String review) {
        this.user = user;
        this.deliveryTimeRating = deliveryTimeRating;
        this.productQualityRating = productQualityRating;
        this.serviceRating = serviceRating;
        this.subject = subject;
        this.review = review;
    }
}
