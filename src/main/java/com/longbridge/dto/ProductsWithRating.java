package com.longbridge.dto;

import com.longbridge.models.Product;

/**
 * Created by Longbridge on 09/05/2018.
 */
public class ProductsWithRating {
    private Product product;

    private int rating;

    public ProductsWithRating() {
    }

    public ProductsWithRating(Product product, int rating) {
        this.product = product;
        this.rating = rating;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
