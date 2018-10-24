package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class WishList extends CommonFields implements Serializable {
    @OneToOne
    private User user;

    @JsonIgnore
    @OneToOne
    private Product product;

    public WishList() {
    }

    public WishList(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
