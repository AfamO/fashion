package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.longbridge.dto.ProductPictureDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 15/08/2018.
 */
@Entity
public class ProductAttribute extends CommonFields implements Serializable {

    private String colourPicture;
    private String colourName;

    @OneToMany(mappedBy = "productAttribute", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ProductSizes> productSizes;


    @OneToMany(mappedBy = "productAttribute", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ProductPicture> productPictures;

    @JsonIgnore
    @ManyToOne
    private Products products;


    public String getColourPicture() {
        return colourPicture;
    }

    public void setColourPicture(String colourPicture) {
        this.colourPicture = colourPicture;
    }

    public List<ProductSizes> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSizes> productSizes) {
        this.productSizes = productSizes;
    }


    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public List<ProductPicture> getProductPictures() {
        return productPictures;
    }

    public void setProductPictures(List<ProductPicture> productPictures) {
        this.productPictures = productPictures;
    }
}

