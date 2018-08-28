package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Longbridge on 14/08/2018.
 */
public class Colour extends CommonFields {

    private String name;

    private String picture;


    @JsonIgnore
    @OneToMany
    private List<ProductPicture> products;




    public Colour() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<ProductPicture> getProducts() {
        return products;
    }

    public void setProducts(List<ProductPicture> products) {
        this.products = products;
    }
}
