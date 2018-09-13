package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Longbridge on 13/09/2018.
 */
@Entity
public class OrderItemProcessingPicture extends CommonFields{
    private String pictureName;
    private String picture;

    @JsonIgnore
    @ManyToOne
    private Items items;

    public OrderItemProcessingPicture(String pictureName, String picture, Items items) {
        this.pictureName = pictureName;
        this.picture = picture;
        this.items = items;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public OrderItemProcessingPicture() {
    }
}
