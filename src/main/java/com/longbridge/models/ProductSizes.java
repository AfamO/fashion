package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProductSizes extends CommonFields{

    private String name;
    private int stockNo;

    @JsonIgnore
    @ManyToOne
    private Products products;

    public ProductSizes(String name, int stockNo, Products products) {
        this.name = name;
        this.stockNo = stockNo;
        this.products = products;
    }

    public ProductSizes() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
}