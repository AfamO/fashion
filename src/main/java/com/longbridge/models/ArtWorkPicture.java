package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Entity
public class ArtWorkPicture extends CommonFields{
    private Long id;
    private String pictureName;
    private String picture;
    @JsonIgnore
    @ManyToOne
    private Products products;

    public ArtWorkPicture() {
    }

    public ArtWorkPicture(Long id, String pictureName, String picture, Products products) {
        this.id = id;
        this.pictureName = pictureName;
        this.picture = picture;
        this.products = products;
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

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
}
