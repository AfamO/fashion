package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Entity
public class MaterialPicture extends CommonFields implements Serializable{
    private Long id;
    private String pictureName;
    private String picture;
    private String materialName;
    private double price;
    @JsonIgnore
    @ManyToOne
    private BespokeProduct bespokeProduct;

    public MaterialPicture() {
    }

    public MaterialPicture(Long id, String pictureName, String picture, String materialName, double price, BespokeProduct bespokeProduct) {
        this.id = id;
        this.pictureName = pictureName;
        this.picture = picture;
        this.materialName = materialName;
        this.price = price;
        this.bespokeProduct = bespokeProduct;
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BespokeProduct getBespokeProduct() {
        return bespokeProduct;
    }

    public void setBespokeProduct(BespokeProduct bespokeProduct) {
        this.bespokeProduct = bespokeProduct;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
