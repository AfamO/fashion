package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class InspireMeProduct extends CommonFields {

    @JsonIgnore
    @ManyToOne
    private InspireMe inspireMe;

    private String name;
    private double price;
    private Long productId;
    private Long productAttributeId;
    private String picture;
    private Long designerId;
    private String designerName;
    private Long productTypeId;

    public InspireMeProduct() {
    }

    public InspireMeProduct(InspireMe inspireMe, String name, double price, Long productId, Long productAttributeId, String picture, Long designerId, String designerName, Long productTypeId) {
        this.inspireMe = inspireMe;
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.productAttributeId = productAttributeId;
        this.picture = picture;
        this.designerId = designerId;
        this.designerName = designerName;
        this.productTypeId = productTypeId;
    }

    public InspireMe getInspireMe() {
        return inspireMe;
    }

    public void setInspireMe(InspireMe inspireMe) {
        this.inspireMe = inspireMe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(Long productAttributeId) {
        this.productAttributeId = productAttributeId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }
}
