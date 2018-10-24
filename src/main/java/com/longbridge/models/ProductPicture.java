package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by longbridge on 10/18/17.
 */
@Entity
public class ProductPicture extends CommonFields implements Serializable {
    private Long id;
    private String pictureName;
    private String picture;


    @JsonIgnore
    @ManyToOne
    private ProductStyle productStyle;

    public ProductStyle getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(ProductStyle productStyle) {
        this.productStyle = productStyle;
    }

    @JsonIgnore
    @ManyToOne
    private ProductColorStyle productColorStyle;


    public ProductPicture() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ProductColorStyle getProductColorStyle() {
        return productColorStyle;
    }

    public void setProductColorStyle(ProductColorStyle productColorStyle) {
        this.productColorStyle = productColorStyle;
    }
}
