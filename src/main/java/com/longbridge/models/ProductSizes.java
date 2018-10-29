package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProductSizes extends CommonFields{

    private String name;
    private int numberInStock;
    private String inStock;

    @JsonIgnore
    @ManyToOne
    private ProductColorStyle productColorStyle;


    public ProductSizes() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberInStock() {
        return numberInStock;
    }

    public void setNumberInStock(int numberInStock) {
        this.numberInStock = numberInStock;
    }

    public ProductColorStyle getProductColorStyle() {
        return productColorStyle;
    }


    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public void setProductColorStyle(ProductColorStyle productColorStyle) {
        this.productColorStyle = productColorStyle;
    }
}