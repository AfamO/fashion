package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProductSizes extends CommonFields{

    private String name;
    private int numberInStock;

    @JsonIgnore
    @ManyToOne
    private ProductColorStyles productAttribute;


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

    public ProductColorStyles getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ProductColorStyles productAttribute) {
        this.productAttribute = productAttribute;
    }

    @Override
    public String toString() {
        return "ProductSizes{" +
                "name='" + name + '\'' +
                ", numberInStock=" + numberInStock +
                ", productAttribute=" + productAttribute +
                '}';
    }
}