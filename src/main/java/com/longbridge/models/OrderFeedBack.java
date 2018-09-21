package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by Longbridge on 20/09/2018.
 */
@Entity
public class OrderFeedBack extends CommonFields {
    private String orderNumber;
    private String email;
    private String subject;
    private String description;
    private String feedBackReaction;



    public OrderFeedBack() {
    }


    public OrderFeedBack(String orderNumber, String email, String subject, String description, String feedBackReaction) {
        this.orderNumber = orderNumber;
        this.email = email;
        this.subject = subject;
        this.description = description;
        this.feedBackReaction = feedBackReaction;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeedBackReaction() {
        return feedBackReaction;
    }

    public void setFeedBackReaction(String feedBackReaction) {
        this.feedBackReaction = feedBackReaction;
    }
}
