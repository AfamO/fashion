package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class WishList extends CommonFields {
    @OneToOne
    private User user;

    @JsonIgnore
    @OneToOne
    private Products products;

    public WishList() {
    }

    public WishList(User user, Products products) {
        this.user = user;
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
}
