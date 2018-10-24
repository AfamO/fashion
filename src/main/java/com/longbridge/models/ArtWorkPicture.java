package com.longbridge.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Entity
public class ArtWorkPicture extends CommonFields implements Serializable {
    private Long id;
    private String pictureName;
    private String picture;
    
    @JsonIgnore
    @ManyToOne
    private BespokeProduct bespokeProduct;

    public ArtWorkPicture() {
    }


    public ArtWorkPicture(Long id, String pictureName, String picture, BespokeProduct bespokeProduct) {
        this.id = id;
        this.pictureName = pictureName;
        this.picture = picture;
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

    public BespokeProduct getBespokeProduct() {
        return bespokeProduct;
    }

    public void setBespokeProduct(BespokeProduct bespokeProduct) {
        this.bespokeProduct = bespokeProduct;
    }
}
