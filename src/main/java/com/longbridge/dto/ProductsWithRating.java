package com.longbridge.dto;

import com.longbridge.models.Products;

/**
 * Created by Longbridge on 09/05/2018.
 */
public class ProductsWithRating {
    private Products products;

    private int rating;

    public ProductsWithRating() {
    }

    public ProductsWithRating(Products products, int rating) {
        this.products = products;
        this.rating = rating;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
