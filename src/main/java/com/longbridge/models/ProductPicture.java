package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class ProductPicture extends CommonFields{
    private Long id;
    private String pictureName;
    private String picture;


    @JsonIgnore
    @ManyToOne
    private ProductAttribute productAttribute;

    @JsonIgnore
    @ManyToOne
    private Products products;


    public ProductPicture() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ProductAttribute getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ProductAttribute productAttribute) {
        this.productAttribute = productAttribute;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
}
