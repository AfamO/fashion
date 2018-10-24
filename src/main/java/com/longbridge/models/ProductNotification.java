package com.longbridge.models;

import javax.persistence.Entity;

/**
 * Created by Longbridge on 10/07/2018.
 */
@Entity
public class ProductNotification extends CommonFields{
    private String email;

    private Long productColorStyleId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getProductColorStyleId() {
        return productColorStyleId;
    }

    public void setProductColorStyleId(Long productColorStyleId) {
        this.productColorStyleId = productColorStyleId;
    }

    public ProductNotification(String email, Long productColorStyleId) {
        this.email = email;
        this.productColorStyleId = productColorStyleId;
    }

    public ProductNotification() {
    }
}
