package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by Longbridge on 10/07/2018.
 */
@Entity
public class ProductNotification extends CommonFields{
    private String email;
    private Long productId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public ProductNotification() {
    }

    public ProductNotification(String email, Long productId) {
        this.email = email;
        this.productId = productId;
    }



}
