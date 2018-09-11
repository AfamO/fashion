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
    private ProductAttribute productAttribute;


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

    public ProductAttribute getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ProductAttribute productAttribute) {
        this.productAttribute = productAttribute;
    }

    @Override
    public String toString() {
        return "ProductSizes{" +
                "name='" + name + '\'' +
                ", stockNo=" + stockNo +
                ", productAttribute=" + productAttribute +
                '}';
    }
}