package com.longbridge.models;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class PictureTag extends CommonFields {
    @ManyToOne
    private EventPictures eventPictures;
    @OneToOne
    private SubCategory subCategory;
    @OneToOne
    private Designer designer;

    private String leftCoordinate;
    private String topCoordinate;
    private String imageSize;

    @OneToOne
    private Products products;


    public EventPictures getEventPictures() {
        return eventPictures;
    }

    public void setEventPictures(EventPictures eventPictures) {
        this.eventPictures = eventPictures;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }

    public String getLeftCoordinate() {
        return leftCoordinate;
    }

    public void setLeftCoordinate(String leftCoordinate) {
        this.leftCoordinate = leftCoordinate;
    }

    public String getTopCoordinate() {
        return topCoordinate;
    }

    public void setTopCoordinate(String topCoordinate) {
        this.topCoordinate = topCoordinate;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
}
